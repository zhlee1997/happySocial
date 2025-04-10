pipeline {
    agent any
    
    environment {
        AWS_ACCOUNT_ID = '565428532910'
        AWS_REGION = "ap-southeast-5"
        ECR_REPO_NAME = "leezonghan19/happysocial"
        IMAGE_NAME_1 = "insta-service-registry.latest"
        IMAGE_NAME_2 = "insta-config-server.latest"
        IMAGE_NAME_3 = "insta-gateway-service.latest"
        IMAGE_NAME_4 = "insta-auth-service.latest"
        IMAGE_NAME_5 = "insta-user-service.latest"
        IMAGE_NAME_6 = "insta-post-service.latest"
        IMAGE_NAME_7 = "insta-media-service.latest"
    }

    stages {
        stage('Pull Image') {
            steps {
                sh '''
                    docker --version
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_1
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_2
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_3
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_4
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_5
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_6
                    docker pull leezonghan19/happysocial-backend:$IMAGE_NAME_7
                    docker image ls
                '''
            }
        }
        
        stage('Authenticate with AWS') {
            steps {
                sh '''
                    export PATH=$PATH:/usr/local/bin
                    aws --version
                    aws s3 ls
                '''
            }
        }
        
        stage('Authenticate with AWS ECR') {
            steps {
                sh '''
                    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
                    aws ecr list-images --repository-name $ECR_REPO_NAME
                '''
            }
        }
        
        stage('Tag Docker Image for AWS ECR') {
            steps {
                sh '''
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_1 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_1
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_2 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_2
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_3 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_3
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_4 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_4
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_5 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_5
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_6 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_6
                    docker tag leezonghan19/happysocial-backend:$IMAGE_NAME_7 $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_7
                '''
            }
        }

        stage('Push Image to AWS ECR') {
            steps {
                sh '''
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_1
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_2
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_3
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_4
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_5
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_6
                    docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO_NAME:$IMAGE_NAME_7
                '''
            }
        }
    }
}
