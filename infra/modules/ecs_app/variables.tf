variable "name" {
  type = string
}

variable "ecs_cluster_name" {
  type = string
}

variable "image_url" {
  type        = string
  description = "Docker image to run from ECR"
}

variable "db_url" {
  type = string
}

variable "db_username" {
  type = string
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "ecs_sg_id" {
  type = string
}
