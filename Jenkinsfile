pipeline {
    agent any
    
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials' // Configure this in Jenkins Credentials
        IMAGE_NAME = 'kiruba11111985/finsecure-pay'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-credentials' // Optional if running on Jenkins master/agent with kubectl access
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Security Checks') {
            steps {
                // Example using dependency-check or Trivy filesystem scan
                echo 'Running security vulnerability scan...'
                sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL . || true'
            }
        }

        stage('Container Build') {
            steps {
                script {
                    appImage = docker.build("${env.IMAGE_NAME}:${env.BUILD_NUMBER}")
                    docker.image("${env.IMAGE_NAME}:${env.BUILD_NUMBER}").tag("latest")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${env.DOCKER_CREDENTIALS_ID}") {
                        docker.image("${env.BUILD_NUMBER}").push()
                        docker.image("latest").push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh """
                    # Update deployment image tag dynamically
                    sed -i 's|image: ${IMAGE_NAME}:.*|image: ${IMAGE_NAME}:${BUILD_NUMBER}|g' deployment.yaml
                    
                    # Apply manifests to cluster
                    kubectl apply -f configmap.yaml
                    kubectl apply -f deployment.yaml
                    kubectl apply -f service.yaml
                    
                    # Verify rollout status
                    kubectl rollout status deployment/finsecure-pay-deployment --timeout=120s
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! FinSecure Pay deployed to Kubernetes.'
        }
        failure {
            echo 'Pipeline failed! Deployment and rollout were aborted.'
        }
    }
}
