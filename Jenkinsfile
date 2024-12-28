pipeline {
    agent any

    stages {
        stage("init"){
            steps {
                script {
                    gv = load "scripts.groovy"
                }
            }
        }

        stage("Building microservice") {
            steps {
                script {
                    gv.buildbuildUsersMicroserviceImage();
                }
            }
        }

        stage("Connecting to Server") {
            steps {
                script {
                    gv.pushImage();
                }
            }
        }

        stage("Copying files to Server") {
            steps {
                script {
                    gv.pushImage();
                }
            }
        }

        stage("Running microservice...") {
            steps {
                script {
                    gv.pushImage();
                }
            }
        }
    }

    
}