/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
    // sh 'mvn clean package'
}

def connectToEc2() {
    echo 'Connecting to EC2'
    sh 'ssh -i "/var/jenkins_home/workspace/kp-my-server.pem" ec2-user@ec2-44-222-201-100.compute-1.amazonaws.com'
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
