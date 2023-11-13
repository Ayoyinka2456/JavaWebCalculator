pipeline {
    agent {
        label 'Build-server'
    }
    tools {
        maven 'Maven'
        jdk 'Java11'
    }
    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/Ayoyinka2456/Jenkins-pipeline1.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Test') {

            steps {
                sh 'mvn test'
                stash(name: 'packaged_code', includes: 'target/*.war')
            }
        }
        stage('Deploy to Tomcat') {
            agent {
                label 'ansible-server'
            }
            stage('Run Ansible') {
                steps {
                    unstash 'packaged_code'
                    script {
                        ansiblePlaybook(
                            playbook: '~/playbook.yml',
                            inventory: '~/hosts.ini'
                        )
                    }
                    sh "sudo mv target/*.war ~/opt/tomcat/webapps/"
                    sh "sudo /opt/tomcat/bin/shutdown.sh && sudo /opt/tomcat/bin/startup.sh"
                }
            }
        }
    }
    post{
        always {
            emailext body: 'Check console output at $BUILD_URL to view the results.', 
            subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', 
            to: 'eas.adeyemi@gmail.com'
        }
    }     
}
