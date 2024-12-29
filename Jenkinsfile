/* groovylint-disable CompileStatic */
pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
    }

    stages {
        stage('init'){
            steps {
                script {
                    gv = load 'scripts.groovy'
                }
            }
        }

        stage('Building microservice') {
            steps {
                script {
                    gv.buildUsersMicroservice()
                }
            }
        }

        stage('Connecting to Server') {
            steps {
                script {
                    gv.connectToEc2()
                }
            }
        }

        stage('Copying files to Server') {
            steps {
                script {
                    gv.copyJarToEc2()
                }
            }
        }

        stage('Running microservice...') {
            steps {
                script {
                    gv.runUsersMicroService()
                }
            }
        }
    }
}