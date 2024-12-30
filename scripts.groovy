/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
    sh 'mvn clean package -DskipTests'
}

def connectToEc2() {
    sshagent(['2']) {
        echo 'Connecting to EC2'
        USER = 'ec2-user'
        HOST = 'ec2-3-88-248-51.compute-1.amazonaws.com'
        sh 'scp -i ${keyfile} ./target/banks-ms-0.0.1-SNAPSHOT.jar ${USER}@${HOST}:~'
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
