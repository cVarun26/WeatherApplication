module "vpc" {
  source   = "./modules/vpc"
  name     = "Atmo"
  vpc_cidr = "10.0.0.0/16"
}

module "rds" {
  source             = "./modules/rds"
  name               = var.project_name
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids
  db_username        = "admin"
  db_password        = "1234567890"
  ecs_sg_id          = aws_security_group.ecs_service.id
}

output "rds_endpoint" {
  value = module.rds.rds_endpoint
}

resource "aws_security_group" "ecs_service" {
  name          = "${var.project_name}-ecs-sg"
  description   = "Allows ECS service to connect outwards"
  vpc_id        = module.vpc.vpc_id

  #To allow ECS tasks to reach any other service
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  #No ingress since ECS will be behind load balancer

  tags = {
    Name = "${var.project_name}-ecs-sg"
  }
}

module "ecs" {
  source            = "./modules/ecs_ec2"
  ec2_key_name      = "weatherapp-key"
  ecs_sg_id         = aws_security_group.ecs_service.id
  name              = var.project_name
  public_subnet_ids = module.vpc.public_subnet_ids
}

module "ecs_app" {
  source             = "./modules/ecs_app"
  db_password        = "1234567890"
  db_url             = "jdbc:mysql://${module.rds.rds_endpoint}/atmo-mysql"
  db_username        = "admin"
  ecs_cluster_name   = module.ecs.ecs_cluster_name
  image_url          = var.image_url
  name               = var.project_name
  public_subnet_ids  = module.vpc.public_subnet_ids
  ecs_sg_id          = aws_security_group.ecs_service.id
}
