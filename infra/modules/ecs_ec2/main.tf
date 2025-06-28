resource "aws_ecs_cluster" "main" {
  name = "${var.name}-ecs-cluster"
}

data "aws_ssm_parameter" "ecs_ami" {
  name = "/aws/service/ecs/optimized-ami/amazon-linux-2/recommended/image_id"
}

resource "aws_launch_template" "ecs_instance_lt" {
  name_prefix   = "${var.name}-ecs-lt"
  image_id      = data.aws_ssm_parameter.ecs_ami.value
  instance_type = "t2.micro"
  key_name      = var.ec2_key_name

  vpc_security_group_ids = [var.ecs_sg_id]

  user_data = base64encode(templatefile("${path.module}/userdata.sh", {
    cluster_name = "${var.name}-ecs-cluster"
  }))

  iam_instance_profile {
    name = "ecsInstanceRole"
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "${var.name}-ecs-ec2"
    }
  }
}

resource "aws_autoscaling_group" "ecs_asg" {
  name                = "${var.name}-ecs-sg"
  max_size            = 2
  min_size            = 0
  desired_capacity    = 1
  vpc_zone_identifier = var.public_subnet_ids
  launch_template {
    id      = aws_launch_template.ecs_instance_lt.id
    version = "$Latest"
  }

  lifecycle {
    create_before_destroy = true
  }

  tag {
    key                 = "Name"
    value               = "${var.name}-ecs-instance"
    propagate_at_launch = true
  }
}