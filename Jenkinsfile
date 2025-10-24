pipeline {
    agent any

    triggers {
        // Trigger on Git changes (polling every minute)
        pollSCM('* * * * *')
        // For real projects, use a webhook instead of polling
    }

    tools {
        maven 'Maven 3.6.3'  // Must match your Jenkins Maven name
        jdk 'Java 11'        // Optional if you set it in Jenkins
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

        stage('Package') {
            steps {
                echo 'Creating deliverable...'
                sh 'mvn package'
            }
        }
    }

    post {
        success {
            echo '✅ Build build build completed successfully!'
        }
        failure {
            echo '❌ Build failed.'
        }
    }
}
