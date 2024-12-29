/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def buildUsersMicroservice() {
    echo 'Building users microservice'
// sh 'mvn clean package'
}

def connectToEc2() {
    sshagent(['2']) {
        echo 'Connecting to EC2'
        sh 'scp -i ${keyfile} ./target/banks-ms-0.0.1-SNAPSHOT.jar ec2-user@ec2-3-88-248-51.compute-1.amazonaws.com:~'
        // sh 'ssh -o StrictHostKeyChecking=no ec2-user@ec2-3-88-248-51.compute-1.amazonaws.com'
        sh '''
            ec2-user@ec2-3-88-248-51.compute-1.amazonaws.com <<'ENDSSH'
            cd ~ && java -jar banks-ms-0.0.1-SNAPSHOT.jar
        ENDSSH'''
    }  
}

def copyJarToEc2() {
    echo 'Copying Files to EC2'
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
