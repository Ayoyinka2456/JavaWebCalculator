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
                        git branch: 'main', url: 'https://github.com/Ayoyinka2456/Jenkins-ansible.git'
                    }
            }
          // git branch: 'main', url: 'https://github.com/Ayoyinka2456/Jenkins-ansible.git'
        }
      }        
        stage('Prepping Tomcat and Maven servers') {
            steps {
                // sh 'cd /home/centos/Jenkins-ansible/workspace/jenkins-ansible/ && ansible-playbook playbook.yml maven.yml -i hosts.ini'
                sh 'cd ${env.WORKSPACE)/Jenkins-ansible/ && ansible-playbook playbook.yml maven.yml -i hosts.ini'
            }
        }
        stage('Build') {
            agent {
                label 'Maven'
            }
            steps {
                sh 'cd ${env.WORKSPACE)/Jenkins-ansible/ && mvn clean install'
            }
        }
        stage('Test') {
            agent {
                label 'Maven'
            }
            steps {
                sh 'cd ${env.WORKSPACE)/Jenkins-ansible/ && mvn test'
                stash(name: 'packaged_code', includes: 'target/*.war')
            }
        }
        stage('Deploy to Tomcat') {
            parallel {
                stage('n1_centos') {
                    agent {
                        label 'n1_centos'
                    }
                    steps {
                        unstash 'packaged_code'
                        sh "sudo rm -rf /opt/tomcat/webapps/*.war"
                        sh "sudo mv target/*.war /opt/tomcat/webapps/"
                        sh "sudo /opt/tomcat/bin/shutdown.sh && sudo /opt/tomcat/bin/startup.sh"
                    }
                }
                stage('n2_ubuntu') {
                    agent {
                        label 'n2_ubuntu'
                    }
                    steps {
                        unstash 'packaged_code'
                        sh "sudo rm -rf /opt/tomcat/webapps/*.war"
                        sh "sudo mv target/*.war /opt/tomcat/webapps/"
                        sh "sudo /opt/tomcat/bin/shutdown.sh && sudo /opt/tomcat/bin/startup.sh"
                    }
                }
            }
        }
    }
}
