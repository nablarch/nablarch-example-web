# DB Subnet Group
resource "aws_db_subnet_group" "performance_web_db_subnet" {
  name = "performance_web_db_subnet_group"
  subnet_ids = [
    aws_subnet.performance_web_private_subnet1.id,
    aws_subnet.performance_web_private_subnet2.id,
  ]
  description = "db subnet for performance test"
  tags = {
    Name = "performance_web_db_subnet_group"
  }
}

# RDS
resource "aws_db_instance" "performance_web_db_instance" {
  identifier = "performance-web-db-instance"
  instance_class = "db.m5.large"
  engine = "postgres"
  engine_version = "12.4"
  allocated_storage = 20
  name = "performance"
  username = "postgres"
  password = "postgres"
  availability_zone = "ap-northeast-1d"
  auto_minor_version_upgrade = false
  performance_insights_enabled = false
  copy_tags_to_snapshot = false
  skip_final_snapshot = true
  db_subnet_group_name = aws_db_subnet_group.performance_web_db_subnet.name
  vpc_security_group_ids = [aws_security_group.performance_web_private_subnet_sg.id]

  tags = {
    Name = "performance-web-db-instance"
  }
}