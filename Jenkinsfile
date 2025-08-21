pipeline {
  agent any

  tools {
    maven 'Maven_3'        // Configure in Jenkins Global Tool Configuration
    jdk 'JDK_17'           // Configure in Jenkins as well
  }

  environment {
    // Configure a SonarQube server named 'SonarServer' in Jenkins (Manage Jenkins > System)
    SONARQUBE_ENV = 'SonarServer'
    // Provide a token credential id named 'sonar-token' in Jenkins Credentials
    SONAR_TOKEN = credentials('sonar-token')
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B -DskipTests=false clean verify'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv("${SONARQUBE_ENV}") {
          sh """mvn sonar:sonar             -Dsonar.projectKey=vulnerable-demo             -Dsonar.login=${SONAR_TOKEN}             -Dsonar.host.url=$SONAR_HOST_URL             -Dsonar.java.binaries=target/classes
          """
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
  }
}
