/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */

def buildUsersMicroservice() {
    echo 'Building users microservice'
    // sh 'mvn clean package -DskipTests'
}

def copyJarToEC2() {
    echo 'Connecting to EC2'
    sh '''scp -i ${keyfile} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ${USER}@${HOST}:~/bank-ms'''
}

def runUsersMicroService() {
    echo 'Running users microservice'
}

return this
