/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */
def user = "ec2-user"
def host = "ec2-52-90-34-240.compute-1.amazonaws.com:~/bank-ms"

def buildUsersMicroservice() {
    echo 'Building users microservice'
    // sh 'mvn clean package -DskipTests'
}

def copyJarToEC2() {
    echo 'Connecting to EC2'
    sh '''scp -i ${keyfile} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ${user}@${host}:~/bank-ms'''
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
