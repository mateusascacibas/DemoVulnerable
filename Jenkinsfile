pipeline {
    agent any

    tools {
        jdk 'JDK_17'
        maven 'Maven_3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mateusascacibas/DemoVulnerable.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

      stage('SonarQube Analysis') {
          steps {
            withSonarQubeEnv('SonarServer') {
              sh '''
                mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                   -Dsonar.projectKey=vulnerable-demo
              '''
            }
          }
        }

    }
}
