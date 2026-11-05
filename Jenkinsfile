pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.x' // Adjust to your global tool configuration name if needed, or omit if default
    }
    
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-creds' // ID of credentials stored in Jenkins
        IMAGE_NAME = 'Kiru11/finsecure-pay'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${env.IMAGE_NAME}:${env.BUILD_NUMBER}")
                    docker.image("${env.IMAGE_NAME}:${env.BUILD_NUMBER}").tag("${env.IMAGE_NAME}:latest")
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${env.DOCKER_CREDENTIALS_ID}") {
                        dockerImage.push()
                        docker.image("${env.IMAGE_NAME}:latest").push()
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}
