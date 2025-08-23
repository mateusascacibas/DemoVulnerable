pipeline {
  agent any
  tools { jdk 'JDK_17'; maven 'Maven_3' }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Tests') {
      steps { sh 'mvn -B -DskipTests=false clean verify' }
      post { always { junit '**/target/surefire-reports/*.xml' } }
    }

   stage('Dependency Check (OWASP)') {
    steps {
      withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
        sh '''
          mvn -B org.owasp:dependency-check-maven:9.2.0:aggregate \
             -Dnvd.api.key=$NVD_API_KEY \
             -Ddata.directory=$WORKSPACE/.depcheck
        '''
      }
    }
    post {
      always {
        archiveArtifacts artifacts: 'target/dependency-check-report.*', fingerprint: true
        publishHTML(target: [reportDir: 'target', reportFiles: 'dependency-check-report.html', reportName: 'OWASP Dependency-Check'])
      }
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

    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: false
        }
      }
    }
  }
}
