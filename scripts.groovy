/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
    // sh 'mvn clean package -DskipTests'
}

def connectToEc2() {
        echo 'Connecting to EC2'
        sh 'scp -i ${keyfile} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ec2-user@ec2-52-90-34-240.compute-1.amazonaws.com:~/bank-ms'
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
