/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */

def buildUsersMicroservice() {
    echo 'Building bank microservice'
    sh 'mvn clean package -DskipTests'
}

def copyJarToEC2() {
    echo 'Copying Jar to EC2'
    sh '''scp -i ${keyfile} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ${USER}@${HOST}:~/${BANK_MS_DIR}/${BANK_MS_FILE}'''
}

def runUsersMicroService() {
    echo 'Running bank microservice'
    sh '''nohup ssh -i ${keyfile} -o StrictHostKeyChecking=no ${USER}@${HOST} "cd ~/${BANK_MS_DIR} &&  java -jar ${BANK_MS_FILE} " &'''
}

return this
