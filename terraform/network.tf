# VPC
resource "aws_vpc" "performance_web_vpc" {
  cidr_block = "172.16.0.0/16"
  enable_dns_hostnames = true
  tags = {
    Name = "performance_web_vpc"
  }
}

# Subnet
resource "aws_subnet" "performance_web_public_subnet" {
  cidr_block = "172.16.1.0/24"
  availability_zone = "ap-northeast-1a"
  vpc_id = aws_vpc.performance_web_vpc.id
  tags = {
    Name = "performance_web_public_subnet"
  }
}

resource "aws_subnet" "performance_web_private_subnet1" {
  cidr_block = "172.16.2.0/24"
  availability_zone = "ap-northeast-1c"
  vpc_id = aws_vpc.performance_web_vpc.id
  tags = {
    Name = "performance_web_private_subnet1"
  }
}

resource "aws_subnet" "performance_web_private_subnet2" {
  cidr_block = "172.16.3.0/24"
  availability_zone = "ap-northeast-1d"
  vpc_id = aws_vpc.performance_web_vpc.id
  tags = {
    Name = "performance_web_private_subnet2"
  }
}

# Internet Gateway
resource "aws_internet_gateway" "performance_web_internet_gateway" {
  vpc_id = aws_vpc.performance_web_vpc.id
  tags = {
    Name = "performance_web_internet_gateway"
  }
}

# EIP
resource "aws_eip" "performance_web_nat_gateway_eip" {
  vpc = true
  tags = {
    Name = "performance_web_nat_gateway_eip"
  }
}

# NAT Gateway
resource "aws_nat_gateway" "performance_web_nat_gateway" {
  allocation_id = aws_eip.performance_web_nat_gateway_eip.id
  subnet_id = aws_subnet.performance_web_public_subnet.id
  tags = {
    Name = "performance_web_nat_gateway"
  }
}

# Route Table
resource "aws_route_table" "performance_web_public_subnet_route_table" {
  vpc_id = aws_vpc.performance_web_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.performance_web_internet_gateway.id
  }

  tags = {
    Name = "performance_web_public_subnet_route_table"
  }
}

resource "aws_route_table_association" "performance_web_public_route_table_association" {
  route_table_id = aws_route_table.performance_web_public_subnet_route_table.id
  subnet_id = aws_subnet.performance_web_public_subnet.id
}

resource "aws_route_table" "performance_private_subnet_route_table" {
  vpc_id = aws_vpc.performance_web_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.performance_web_nat_gateway.id
  }

  tags = {
    Name = "performance_web_private_subnet_route_table"
  }
}

resource "aws_route_table_association" "performance_web_private_route_table_association1" {
  route_table_id = aws_route_table.performance_private_subnet_route_table.id
  subnet_id = aws_subnet.performance_web_private_subnet1.id
}

resource "aws_route_table_association" "performance_web_private_route_table_association2" {
  route_table_id = aws_route_table.performance_private_subnet_route_table.id
  subnet_id = aws_subnet.performance_web_private_subnet2.id
}

# Security Group
resource "aws_security_group" "performance_web_bastion_sg" {
  name = "performance_web_bastion_sg"
  vpc_id = aws_vpc.performance_web_vpc.id

  # 特定のIPからのSSHのみ許可
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = var.ssh_client_cidr_list
  }

  # 外に出るのは自由
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "performance_web_bastion_sg"
  }
}

resource "aws_security_group" "performance_web_private_subnet_sg" {
  name = "performance_web_private_subnet_sg"
  vpc_id = aws_vpc.performance_web_vpc.id

  # bastion からの SSH は許可
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    security_groups = [aws_security_group.performance_web_bastion_sg.id]
  }

  # private subnet 内での通信は自由
  ingress {
    from_port = 0
    to_port = 65535
    protocol = "tcp"
    cidr_blocks = [
      aws_subnet.performance_web_private_subnet1.cidr_block,
      aws_subnet.performance_web_private_subnet2.cidr_block,
    ]
  }

  ingress {
    from_port = -1
    to_port = -1
    protocol = "icmp"
    cidr_blocks = [
      aws_subnet.performance_web_private_subnet1.cidr_block,
      aws_subnet.performance_web_private_subnet2.cidr_block,
    ]
  }

  # 外に出るのは自由
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "performance_web_private_subnet_sg"
  }
}