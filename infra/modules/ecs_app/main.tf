resource "aws_ecs_task_definition" "atmo" {

  family                   = "${var.name}-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["EC2"]
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([
    {
      name         = "atmo"
      image        = var.image_url
      essential    = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ],
      environment = [
        {
          name  = "SPRING_DATASOURCE_URL"
          value = var.db_url
        },
        {
          name  = "SPRING_DATASOURCE_USERNAME"
          value = var.db_username
        },
        {
          name  = "SPRING_DATASOURCE_PASSWORD"
          value = var.db_password
        }
      ],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          awslogs-group         = "/ecs/atmo"
          awslogs-region        = "ap-south-1"
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "atmo_service" {
  name            = "${var.name}-service"
  cluster         = var.ecs_cluster_name
  task_definition = aws_ecs_task_definition.atmo.arn
  desired_count   = 1
  launch_type     = "EC2"

  deployment_minimum_healthy_percent = 100
  deployment_maximum_percent         = 200

  network_configuration {
    subnets          = var.public_subnet_ids
    security_groups  = [var.ecs_sg_id]
  }

  depends_on = [aws_ecs_task_definition.atmo]
}

