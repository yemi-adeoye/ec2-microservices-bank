/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
// sh 'mvn clean package'
}

def connectToEc2() {
    sshagent(['2']) {
        echo 'Connecting to EC2'
        sh 'ls'
        // sh 'ssh -o StrictHostKeyChecking=no ec2-user@ec2-18-206-39-47.compute-1.amazonaws.com'
        sh 'scp /target/banks-ms-0.0.1-SNAPSHOT.jar ec2-user@ec2-18-206-39-47.compute-1.amazonaws.com:/bank'
        sh 'java -jar banks-ms-0.0.1-SNAPSHOT.jar'
    }  
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
