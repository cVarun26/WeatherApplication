variable "name" {
  type = string
}

variable "ecs_sg_id" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "ec2_key_name" {
  type        = string
  description = "EC2 key pair for SSH"
}