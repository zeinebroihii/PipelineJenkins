pipeline {
    agent any

    triggers {
        // Trigger on Git changes (polling every minute)
        pollSCM('* * * * *')
        // For real projects, use a webhook instead of polling
    }

    tools {
        maven 'Maven 3.9.11' // Align with the repo's Maven wrapper version
        jdk 'Java 25'        // Target JDK for the upgraded runtime
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning target directory...'
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling project...'
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Creating deliverable...'
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        success {
            echo '✅ Build buildddd build completed successfully!'
        }
        failure {
            echo '❌ Build failed.'
        }
    }
}
