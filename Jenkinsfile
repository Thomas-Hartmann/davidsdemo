pipeline {
  agent any
  options { disableConcurrentBuilds() }
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean build -x test'
        sh 'docker build -t tomcatapp/myapp .'
      }
    }

    stage('Deploy') {
      steps {
        sh 'docker stop myApp || :'
        sh 'docker rm myApp -f || :'
        sh '''
docker run \
  -d \
  --name myApp \
  -p 8081:8080 \
  tomcatapp/myapp:latest
'''
      }
    }
  }
  post {
    always {
      deleteDir()
    }
  }
}
