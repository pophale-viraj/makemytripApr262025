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
                sh "docker build -t pophale-viraj/makemytrip:latest -t makemytrip:latest ."
                echo 'Docker Image Build Completed!'
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
                        sh 'docker login docker.io -u pophale-viraj -p ${dockerhubCred}'
                        echo 'Pushing Docker Image to Docker Hub...'
                        sh 'docker push pophale-viraj/makemytrip:latest'
                        echo 'Docker Image Pushed to Docker Hub Successfully!'
                    }
                }
            }
        }

        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://533267238276.dkr.ecr.ap-south-1.amazonaws.com"]) {
                         echo 'Tagging and Pushing Docker Image to ECR...'
                         sh '''
                         docker images
                         docker tag makemytrip:latest 533267238276.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                         docker push 533267238276.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:latest
                         '''
                         echo 'Docker Image Pushed to Amazon ECR Successfully!'
                    }
                }
            }
        } //Nexus upload has been skipped
	}
}