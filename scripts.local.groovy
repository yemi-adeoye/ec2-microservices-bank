/* groovylint-disable CompileStatic, FactoryMethodName, MethodReturnTypeRequired, NoDef */

def buildUsersMicroservice() {
    echo 'Building users microservice'
    sh 'mvn clean package -DskipTests'
}

def copyJarToEC2() {
    echo 'Copying Jar to EC2 Mock'
    sh '''echo${LOCAL_EC2_PASSWORD}'''
    // sh '''scp -p ${LOCAL_EC2_PASSWORD} -o StrictHostKeyChecking=no ./target/banks-ms-0.0.1-SNAPSHOT.jar ${LOCAL_USER}@${BANK_MS_LOCALHOST}:~/${BANK_MS_DIR}/${BANK_MS_FILE}'''
}

def runUsersMicroService() {
    echo 'Running users microservice'
    sh '''nohup ssh -i ${keyfile} -o StrictHostKeyChecking=no ${USER}@${HOST} "cd ${BANK_MS_DIR} &&  java -jar ${BANK_MS_FILE} " &'''
}

return this
