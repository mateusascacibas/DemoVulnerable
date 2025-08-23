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
        set -e

        # diretório de cache do Dependency-Check (acelera as próximas execuções)
        DC_CACHE="$WORKSPACE/.depcheck"
        mkdir -p "$DC_CACHE"

        # só para validar que a credencial chegou (não exibe a key):
        echo "NVD key length: ${#NVD_API_KEY}"

        # roda o OWASP com delay para evitar 403/404 da NVD e sem derrubar a pipeline por erro de atualização
        mvn -B org.owasp:dependency-check-maven:9.2.0:aggregate \
           -Dnvd.api.key="$NVD_API_KEY" \
           -Dnvd.api.delay=6000 \
           -Ddata.directory="$DC_CACHE" \
           -DfailOnError=false || echo "[WARN] Dependency-Check terminou com warning/erro; seguindo."

        # só informa se o relatório saiu
        if [ -f target/dependency-check-report.html ]; then
          echo "[INFO] OWASP report gerado com sucesso."
        else
          echo "[WARN] OWASP report não foi gerado (provável bloqueio temporário da NVD)."
        fi
      '''
    }
  }
  post {
    always {
      // não falhar caso o relatório não exista
      archiveArtifacts artifacts: 'target/dependency-check-report.*', fingerprint: true, allowEmptyArchive: true

      // publicar HTML só se existir
      script {
        if (fileExists('target/dependency-check-report.html')) {
          publishHTML(target: [
            reportDir: 'target',
            reportFiles: 'dependency-check-report.html',
            reportName: 'OWASP Dependency-Check'
          ])
        } else {
          echo 'Relatório OWASP ausente; pulando publishHTML.'
        }
      }
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
