#!groovy

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { //Checking out the repo
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/jackson-json-crypto.git']]]
                echo env.GIT_BRANCH
                sh 'echo $GIT_BRANCH'
            }
        }



        stage('Build') {
            steps { //Build using jenkins
                sh './gradlew clean build  test'
            }
        }

        stage('Jar') {
            steps { //Make a jar file
                sh './gradlew jar'
            }
        }

        stage('branch') {
            steps { //branch check
                echo env.GIT_BRANCH
                sh 'echo $GIT_BRANCH'
            }
        }

        stage ('develop') {
            when {
                expression { env.GIT_BRANCH == 'origin/develop' }
            }
            steps {
                echo "Hello origin/develop"
            }
        }

        stage ('release') {
            when {
                expression { env.GIT_BRANCH == 'origin/release' }
            }
            steps {
                echo "Hello origin/release"
            }
        }

        stage ('master') {
            when {
                expression { env.GIT_BRANCH == 'origin/master' }
            }
            steps {
                echo "Hello origin/master"
            }
        }


    }
    post {
        success {
            junit testResults: '**/test-results/test/TEST-*.xml'
            jacoco execPattern: '**/jacoco/jacocoTest.exec'
            recordIssues enabledForFailure: true, tool: spotBugs(pattern: '**/reports/spotbugs/*.xml')
        }
    }
}