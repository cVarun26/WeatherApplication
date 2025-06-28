resource "aws_db_subnet_group" "rds_subnets" {
  name       = "${var.name}-rds-subnet-group"
  subnet_ids = var.private_subnet_ids
  tags = {
    Name = "${var.name}-rds-subnet-group"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "${var.name}-rds-sg"
  description = "Allow MySQL traffic from ECS only"
  vpc_id      = var.vpc_id

  ingress {
    description = "MySQL from ECS"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [var.ecs_sg_id]           #From ECS security group
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.name}-rds-sg"
  }
}

resource "aws_db_instance" "mysql" {
  instance_class         = "db.t3.micro"
  identifier             = "${var.name}-mysql"
  engine                 = "mysql"
  engine_version         = "8.0"
  allocated_storage      = 20
  username               = var.db_username
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.rds_subnets.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible    = false
  skip_final_snapshot    = true
  deletion_protection    = false
  multi_az               = false

  tags = {
    Name = "${var.name}-mysql"
  }
}