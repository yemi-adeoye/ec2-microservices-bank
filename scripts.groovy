/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
    sh 'mvn clean package -DskipTests'
}

def connectToEc2() {
    sshagent(['ec2-kp']) {
        echo 'Connecting to EC2'
        sh 'scp -i ${keyfile} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ec2-user@ec2-52-90-34-240.compute-1.amazonaws.com:~'
        sh 'ssh -i ${keyfile} ${USER}@${HOST} "java -jar banks-ms-0.0.1-SNAPSHOT.jar"'
    }
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
