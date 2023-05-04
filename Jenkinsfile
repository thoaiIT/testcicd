pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo "Hello Jenkins"
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'githubBE', url: 'https://github.com/thoaiIT/testcicd.git']]])
            }
        }
    }
}
