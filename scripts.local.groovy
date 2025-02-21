/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */

def buildUsersMicroservice() {
    echo 'Building bank microservice'
    sh 'mvn clean package -DskipTests'
}

def copyJarToEC2() {
    echo 'Copying Jar to EC2 Mock'
    sh '''sshpass -p ${LOCAL_EC2_PASSWORD} scp -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ${USER}@${LOCALHOST}:/${BANK_MS_DIR}/${BANK_MS_FILE}'''
}

def runUsersMicroService() {
    echo 'Running bank microservice'
    sh '''nohup sshpass -p ${LOCAL_EC2_PASSWORD} ssh ${USER}@${LOCALHOST} " java -jar /${BANK_MS_DIR}/${BANK_MS_FILE} " &'''
}

return this
