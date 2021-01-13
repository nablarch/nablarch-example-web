locals {
  init_script_common = <<-SHELL_SCRIPT
    # Locale の設定
    localectl set-locale LANG=ja_JP.UTF-8
    # Timezone の設定
    timedatectl set-timezone Asia/Tokyo
    # 変数宣言
    USER_HOME=/home/ec2-user
    SHELL_SCRIPT

  init_script_deploy_ssh_key = <<-SHELL_SCRIPT
    echo '${file("./ssh-key/performance")}' > $${USER_HOME}/.ssh/id_rsa
    chown ec2-user:ec2-user $${USER_HOME}/.ssh/id_rsa
    chmod 600 $${USER_HOME}/.ssh/id_rsa
    SHELL_SCRIPT

  init_script_install_jdk = <<-SHELL_SCRIPT
    # Download JDK
    cd $${USER_HOME}
    wget https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk${var.jdk_version}-b01/OpenJDK8U-jdk_x64_linux_hotspot_${var.jdk_version}b01.tar.gz

    # 解凍
    tar -xvzf OpenJDK8U-jdk_x64_linux_hotspot_${var.jdk_version}b01.tar.gz
    chown -R ec2-user:ec2-user jdk${var.jdk_version}-b01

    # 環境変数設定
    echo 'export JAVA_HOME=$HOME/jdk${var.jdk_version}-b01' >> $${USER_HOME}/.bash_profile
    echo 'export PATH=$PATH:$JAVA_HOME/bin' >> $${USER_HOME}/.bash_profile
    SHELL_SCRIPT

  init_script_install_git = <<-SHELL_SCRIPT
    yum install -y git
    SHELL_SCRIPT

  init_script_install_maven = <<-SHELL_SCRIPT
    cd $${USER_HOME}
    wget https://archive.apache.org/dist/maven/maven-3/${var.maven_version}/binaries/apache-maven-${var.maven_version}-bin.tar.gz
    tar -zxvf apache-maven-${var.maven_version}-bin.tar.gz
    echo 'export PATH=$PATH:$HOME/apache-maven-${var.maven_version}/bin' >> $${USER_HOME}/.bash_profile

    # setup config
    mkdir $${USER_HOME}/.m2
    echo '${file("./config/settings.xml")}' > $${USER_HOME}/.m2/settings.xml

    chown -R ec2-user:ec2-user $${USER_HOME}/apache-maven-${var.maven_version}
    chown -R ec2-user:ec2-user $${USER_HOME}/.m2
    SHELL_SCRIPT
}


# Key Pair
resource "aws_key_pair" "performance_web_key_pair" {
  key_name = "performance_web_key_pair"
  public_key = file("./ssh-key/performance.pub")
  tags = {
    Name = "performance_web_key_pair"
  }
}

# EC2 Instance
resource "aws_instance" "performance_web_bastion_instance" {
  ami = "ami-01748a72bed07727c"
  instance_type = "t2.micro"
  key_name = aws_key_pair.performance_web_key_pair.id
  subnet_id = aws_subnet.performance_web_public_subnet.id
  vpc_security_group_ids = [aws_security_group.performance_web_bastion_sg.id]
  user_data = <<-SHELL_SCRIPT
    #!/bin/bash
    ${local.init_script_common}
    ${local.init_script_deploy_ssh_key}
    SHELL_SCRIPT

  tags = {
    Name = "performance_web_bastion_instance"
  }
}

resource "aws_instance" "performance_web_ap_instance" {
  ami = "ami-01748a72bed07727c"
  instance_type = "m5.large"
  key_name = aws_key_pair.performance_web_key_pair.id
  subnet_id = aws_subnet.performance_web_private_subnet1.id
  vpc_security_group_ids = [aws_security_group.performance_web_private_subnet_sg.id]
  private_ip = "172.16.2.10"
  tags = {
    Name = "performance_web_ap_instance"
  }
  depends_on = [aws_db_instance.performance_web_db_instance]
  user_data = <<-SHELL_SCRIPT
    #!/bin/bash
    ${local.init_script_common}
    ${local.init_script_install_jdk}
    ${local.init_script_install_git}
    ${local.init_script_install_maven}

    # Make log directory
    mkdir $${USER_HOME}/logs
    chown ec2-user:ec2-user $${USER_HOME}/logs

    # Install Tomcat
    cd $${USER_HOME}
    wget https://archive.apache.org/dist/tomcat/tomcat-9/v${var.tomcat_version}/bin/apache-tomcat-${var.tomcat_version}.tar.gz

    tar -xvzf apache-tomcat-${var.tomcat_version}.tar.gz

    echo 'TOMCAT_HOME=$HOME/apache-tomcat-${var.tomcat_version}' >> $${USER_HOME}/.bash_profile

    TOMCAT_HOME=$${USER_HOME}/apache-tomcat-${var.tomcat_version}
    cd $${TOMCAT_HOME}/webapps
    rm -rf ROOT docs examples host-manager manager

    cat <<EOF > $${TOMCAT_HOME}/bin/setenv.sh
    #!/bin/bash
    export CATALINA_OPTS="\$CATALINA_OPTS -Xmx512m"

    export NABLARCH_DB_URL=jdbc:postgresql://${aws_db_instance.performance_web_db_instance.endpoint}/performance
    export AWS_REGION=ap-northeast-1
    export AWS_ACCESS_KEY_ID=${var.aws_access_key}
    export AWS_SECRET_ACCESS_KEY=${var.aws_secret_access_key}
    EOF

    chown -R ec2-user:ec2-user $${TOMCAT_HOME}
    SHELL_SCRIPT
}

resource "aws_instance" "performance_web_jmeter_instance" {
  ami = "ami-01748a72bed07727c"
  instance_type = "m5.large"
  key_name = aws_key_pair.performance_web_key_pair.id
  subnet_id = aws_subnet.performance_web_private_subnet1.id
  vpc_security_group_ids = [aws_security_group.performance_web_private_subnet_sg.id]
  private_ip = "172.16.2.11"
  tags = {
    Name = "performance_web_jmeter_instance"
  }
  depends_on = [
    aws_db_instance.performance_web_db_instance,
    aws_instance.performance_web_ap_instance
  ]
  user_data = <<-SHELL_SCRIPT
    #!/bin/bash
    ${local.init_script_common}
    ${local.init_script_install_jdk}
    ${local.init_script_deploy_ssh_key}
    ${local.init_script_install_git}
    ${local.init_script_install_maven}

    # Environment Variable
    echo 'export DB_HOST=${aws_db_instance.performance_web_db_instance.address}' >> $${USER_HOME}/.bash_profile

    # Install JMeter
    cd $${USER_HOME}
    wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${var.jmeter_version}.tgz
    tar -xvzf apache-jmeter-${var.jmeter_version}.tgz
    chown -R ec2-user:ec2-user apache-jmeter-${var.jmeter_version}
    echo 'export PATH=$PATH:$HOME/apache-jmeter-${var.jmeter_version}/bin' >> $${USER_HOME}/.bash_profile

    # Clone nablarch-example-web
    su - ec2-user <<EOF
    git clone https://github.com/nablarch/nablarch-example-web.git
    cd nablarch-example-web

    # Build old application
    git checkout ${var.git_old_tag_name}
    mvn -DskipTests -Ddb.host=${aws_db_instance.performance_web_db_instance.address} package
    scp -o StrictHostKeyChecking=no target/${var.old_war_final_name}.war ec2-user@172.16.2.10:~/

    # Build new application
    git checkout ${var.git_new_tag_name}
    mvn -DskipTests -Ddb.host=${aws_db_instance.performance_web_db_instance.address} package
    scp target/${var.new_war_final_name}.war ec2-user@172.16.2.10:~/

    # Checkout JMeter tool branch
    git checkout -b ${var.git_branch_name} origin/${var.git_branch_name}
    EOF
    SHELL_SCRIPT
}