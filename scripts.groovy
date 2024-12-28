def buildUsersMicroservice(){
    echo "Building users microservice"
    sh 'mvn build'
}

def connectToEc2(){
    echo "Building users microservice"
}

def copyJarToEc2(){
    echo "Copying Files to EC2"
}

def runUsersMicroService(){
    echo "Running users microservice"
}

return this