output "performance_bastion_instance_id" {
  value = aws_instance.performance_web_bastion_instance.id
}

output "performance_ap_instance_id" {
  value = aws_instance.performance_web_ap_instance.id
}

output "performance_jmeter_instance_id" {
  value = aws_instance.performance_web_jmeter_instance.id
}

output "rds_endpoint" {
  value = aws_db_instance.performance_web_db_instance.endpoint
}

output "rds_host" {
  value = aws_db_instance.performance_web_db_instance.address
}