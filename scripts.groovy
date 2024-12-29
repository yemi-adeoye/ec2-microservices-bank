/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
    // sh 'mvn clean package'
}

def connectToEc2() {
    echo 'Connecting to EC2'
    sh 'cd /var/jenkins_home/workspace'
    sh 'ls'
    sh 'ssh -T -i kp-my-server.pem ec2-user@ec2-52-90-228-109.compute-1.amazonaws.com'
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
