pipeline {
    agent any

    environment {
        APP_NAME = "rtb-auction-service"
        DEPLOY_DIR = "/home/ec2-user/rtb-auction-service"
        BACKUP_DIR = "/home/ec2-user/rtb-auction-service-backup"
        HEALTH_URL = "http://localhost/health"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew clean bootJar --no-daemon'
            }
        }

        stage('Pre Deploy Backup') {
            steps {
                sh '''
                sudo rm -rf ${BACKUP_DIR}
                if [ -d ${DEPLOY_DIR} ]; then
                  sudo cp -r ${DEPLOY_DIR} ${BACKUP_DIR}
                fi
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                sudo rsync -a --delete \
                  --exclude='.git' \
                  --exclude='.gradle' \
                  --exclude='build/tmp' \
                  ./ ${DEPLOY_DIR}/

                cd ${DEPLOY_DIR}
                docker-compose down
                docker-compose up -d --build
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                sleep 30
                curl -f ${HEALTH_URL}
                '''
            }
        }
    }

    post {
        success {
            echo "Deployment successful"
        }

        failure {
            echo "Deployment failed. Rolling back..."
            sh '''
            if [ -d ${BACKUP_DIR} ]; then
              sudo rm -rf ${DEPLOY_DIR}
              sudo cp -r ${BACKUP_DIR} ${DEPLOY_DIR}
              cd ${DEPLOY_DIR}
              docker-compose up -d --build
            fi
            '''
        }
    }
}
