terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "ap-southeast-1"
}

resource "aws_vpc" "insta_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.insta_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
}

resource "aws_security_group" "insta_sg" {
  vpc_id = aws_vpc.insta_vpc.id

  ingress {
    from_port   = 5432 # PostgreSQL
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Restrict this in production
  }

  ingress {
    from_port   = 6379 # Redis
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 5672 # RabbitMQ
    to_port     = 5672
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 15672 # RabbitMQ UI
    to_port     = 15672
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # ✅ Allow outbound traffic to the internet (Port 443 for HTTPS)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1" # Allows all outbound traffic
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_internet_gateway" "insta_igw" {
  vpc_id = aws_vpc.insta_vpc.id
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.insta_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.insta_igw.id
  }
}

resource "aws_route_table_association" "public_rt_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_ecs_cluster" "insta_cluster" {
  name = "insta-cluster"
}

# Redis Task
resource "aws_ecs_task_definition" "redis_task" {
  family                   = "redis-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512

  container_definitions = jsonencode([
    {
      name      = "redis-container"
      image     = "redis:latest"
      cpu       = 256
      memory    = 512
      essential = true
      portMappings = [{
        containerPort = 6379
        hostPort      = 6379
        protocol      = "tcp"
      }]
    }
  ])
}

# Redis Fargate
resource "aws_ecs_service" "redis_service" {
  name            = "redis-service"
  cluster         = aws_ecs_cluster.insta_cluster.id
  task_definition = aws_ecs_task_definition.redis_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.public_subnet.id]
    security_groups  = [aws_security_group.insta_sg.id]
    assign_public_ip = true
  }

  desired_count = 1
}

# Postgres Task
resource "aws_ecs_task_definition" "postgres_task" {
  family                   = "postgres-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 512
  memory                   = 1024

  container_definitions = jsonencode([
    {
      name      = "postgres-container"
      image     = "postgres:latest"
      cpu       = 512
      memory    = 1024
      essential = true
      environment = [
        { name = "POSTGRES_USER", value = "admin" },
        { name = "POSTGRES_PASSWORD", value = "yourpassword" },
        { name = "POSTGRES_DB", value = "instadb" }
      ]
      portMappings = [{
        containerPort = 5432
        hostPort      = 5432
        protocol      = "tcp"
      }]
    }
  ])
}

# Postgres Fargate
resource "aws_ecs_service" "postgres_service" {
  name            = "postgres-service"
  cluster         = aws_ecs_cluster.insta_cluster.id
  task_definition = aws_ecs_task_definition.postgres_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.public_subnet.id]
    security_groups  = [aws_security_group.insta_sg.id]
    assign_public_ip = true
  }

  desired_count = 1
}
