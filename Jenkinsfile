pipeline {
    agent { label 'docker-agent' }
    environment {
        ECR_REPO = 'leezonghan19/link-app'
        IMAGE_TAG = 'latest'
        ECR_REGISTRY = '565428532910.dkr.ecr.us-east-1.amazonaws.com'
    }
    stages {
        stage('Clone repository') {
            steps {
                git branch: 'main', credentialsId: 'my-vm-token', url: 'https://github.com/zhlee1997/happySocial.git'
            }
        }
        stage('CD & Maven Build') {
            steps {
                script {
                    docker.image('maven:3.9-eclipse-temurin-21-alpine').inside {
                        sh '''
                            cd insta-service-registry
                            mvn clean package -DskipTests
                            cd ../insta-config-server
                            mvn clean package -DskipTests
                            cd ../insta-gateway-service
                            mvn clean package -DskipTests
                        '''
                    }
                }
            }
        }
        // AWS CLI + creds -> Dockerfile automate
        stage('Login to ECR') {
            steps {
                sh '''
                    aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 565428532910.dkr.ecr.us-east-1.amazonaws.com
                '''
            }
        }
        stage('Build Image with Pack & Publish') {
            steps {
                sh '''
                    cd insta-service-registry
                      pack build 565428532910.dkr.ecr.us-east-1.amazonaws.com/leezonghan19/link-app:latest \
                        --builder paketobuildpacks/builder-jammy-tiny \
                        --path target/*.jar \
                        --publish
                    cd ../insta-config-server
                      pack build 565428532910.dkr.ecr.us-east-1.amazonaws.com/linkspark/config-service:latest \
                        --builder paketobuildpacks/builder-jammy-tiny \
                        --path target/*.jar \
                        --publish
                      cd ../insta-gateway-service
                      pack build 565428532910.dkr.ecr.us-east-1.amazonaws.com/linkspark/api-gateway:latest \
                        --builder paketobuildpacks/builder-jammy-tiny \
                        --path target/*.jar \
                        --publish
                '''
            }
        }
        // stage('Push Image to ECR') {
        //     steps {
        //         script {
        //             docker.image("${ECR_REGISTRY}/${ECR_REPO}:${IMAGE_TAG}").push()
        //         }
        //     }
        // }
    }
}
