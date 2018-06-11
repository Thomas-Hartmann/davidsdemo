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

    stage('Setup database') {
      when {
        expression {
          MYSQL_EXISTS=sh(returnStdout: true, script: "docker ps | grep mysql || :")
          return !MYSQL_EXISTS?.trim()
        }
      }
      steps {
        sh 'docker stop mysql || :'
        sh 'docker rm mysql || :'
        sh 'docker pull mysql:5'
        sh '''
        docker run \
          -d \
          --name mysql \
          -e MYSQL_DATABASE=glazier \
          -e MYSQL_USER=glazier \
          -e MYSQL_PASSWORD=secret \
          -e MYSQL_ROOT_PASSWORD=root \
          -v /tmp/mysql:/var/lib/mysql \
          -p 3306:3306 \
          mysql:5
        '''
        sh '''
        docker exec -i mysql bash -c "
        while ! mysqladmin ping -h '127.0.0.1' -u root -proot --silent; do
            sleep 1
        done
        "
        '''
        sh '''
        docker exec -i mysql \
          mysql \
          -h localhost \
          -P 3306 \
          -u root -proot << EOF
          GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root';
EOF
        '''
        sh '''
        docker exec -i mysql \
          mysql \
          -h 127.0.0.1 \
          -P 3306 \
          -u root -proot \
          glazier < migrations/glazier.sql
        '''
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
          --add-host="mysql:$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql)" \
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
