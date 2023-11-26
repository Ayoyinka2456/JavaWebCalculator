    pipeline {
    agent {
        label 'ansible'
    }
    tools {
        git 'Default'
    }
    stages {
        stage('Clone Repository') {
            steps {
                script {
                    // Specify the target directory
                    def targetDir = "${env.WORKSPACE}/"
                    // Clone the repository into the specified directory
                    dir(targetDir) {
                        git branch: 'main', url: 'https://github.com/Ayoyinka2456/JavaWebCalculator.git'
                    }
                }
            }
        }
        stage('Prepping Tomcat and Maven servers') {
            steps {
                sh "cd ${env.WORKSPACE}/ && ansible-playbook playbook.yml maven.yml -i hosts.ini"
            }
        }
        stage('Build') {
            agent {
                label 'Maven'
            }
            steps {
                sh "cd ${env.WORKSPACE}/JavaWebCalculator/ && mvn clean install"
            }
        }
        stage('Test') {
            agent {
                label 'Maven'
            }
            steps {
                sh "cd ${env.WORKSPACE}/JavaWebCalculator/ && mvn test"
                stash(name: 'packaged_code', includes: 'JavaWebCalculator/target/*.war')
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                unstash 'packaged_code'
                sh "sudo rm -rf ${env.WORKSPACE}/*.war"
                sh "sudo mv JavaWebCalculator/target/*.war ${env.WORKSPACE}/"                
                sh "cd ${env.WORKSPACE}/ && ansible-playbook deploy.yml -i hosts.ini"
                // sh 'sudo ansible g1 -m copy -a "src=./*.war dest=/opt/tomcat/webapps/" -i hosts.ini'

                
            }
        }
    }
}
