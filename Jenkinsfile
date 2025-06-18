pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
    }

    tools {
        maven 'mvn_3.9.10'  // Make sure this Maven tool is defined in Jenkins global tools
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Starting Code Compilation...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }

        stage('Code QA Execution') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully!'
            }
        }

        stage('Code Package') {
            steps {
                echo 'Creating WAR Artifact...'
                sh 'mvn clean package'
                echo 'WAR Artifact Created Successfully!'
            }
        }

        stage('Build & Tag Docker Image') {
            steps {
                echo 'Building Docker Image with Tags...'
                sh "docker build -t pophaleviraj/makemytrip:latest -t makemytrip:latest ."
                echo 'Docker Image BuildCompleted!'
            }
        }

        stage('Docker Image Scanning') {
            steps {
                echo 'Scanning Docker Image with Trivy...'
                echo 'Docker Image Scanning Completed!'
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhubCred', variable: 'dockerhubCred')]) {
                        sh 'docker login docker.io -u pophaleviraj -p ${dockerhubCred}'
                        echo 'Pushing Docker Image to Docker Hub...'
                        sh 'docker push pophaleviraj/makemytrip:latest'
                        echo 'Docker Image Pushed to Docker Hub Successfully!'
                    }
                }
            }
        }

        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://861276077332.dkr.ecr.ap-south-1.amazonaws.com"]) {
                         echo 'Tagging and Pushing Docker Image to ECR...'
                         sh '''
                         docker images
                         docker tag makemytrip:latest 861276077332.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                         docker push 861276077332.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                         '''
                         echo 'Docker Image Pushed to Amazon ECR Successfully!'
                    }
                }
            }
        }

        stage('Upload Docker Image to Nexus') {
            steps {
                script {
                     withCredentials([usernamePassword(credentialsId: 'nexuscred', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                          sh 'docker login http://65.1.95.70:8085/repository/makemytrip/ -u admin -p ${PASSWORD}'
                          echo "Push Docker Image to Nexus : In Progress"
                          sh 'docker tag makemytrip 65.1.95.70:8085/makemytrip:latest'
                          sh 'docker push 65.1.95.70:8085/makemytrip'
                          echo "Push Docker Image to Nexus : Completed"
                     }
                }
            }
        }

        stage('Sonarqube') {
            environment {
                scannerHome = tool 'qube'
            }
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh "${scannerHome}/bin/sonar-scanner"
                    sh 'mvn sonar:sonar'
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Cleanup Docker Images') {
            steps {
                echo 'Cleaning up local Docker images...'
                sh "docker rmi -f ${DOCKER_IMAGE}:latest || true"
                sh "docker rmi -f ${ECR_REPO}:latest || true"
                echo 'Local Docker images deleted successfully!'
            }
        }
	}
}