pipeline {
    agent {
        label 'DS agent'
    }

    environment {
        MVN_SETTINGS = '/etc/m2/settings.xml' //This should be changed in Jenkins config for the DS agent
        PROJECT = 'ds-license'
        PROJECT_VERSION = "${env.BRANCH_NAME}-${env.ORIGINAL_JOB}-${env.PROJECT}-SNAPSHOT"
        BUILD_TO_TRIGGER = 'ds-present'
    }

    triggers {
        // This triggers the pipeline when a PR is opened or updated or so I hope
        githubPush()
    }

    parameters {
        string(name: 'PR_ID', defaultValue: 'NOT_A_PR', description: 'NOT_A_PR if not part of PR and otherwise the name of the first outer most job og the PR')
        string(name: 'ORIGINAL_JOB', defaultValue: "${env.PROJECT}", description: 'Top-most job')
        string(name: 'TARGET_BRANCH', defaultValue: "${env.CHANGE_TARGET}", description: 'Target branch for PR')
    }

    stages {
        stage('Echo Environment Variables') {
            steps {
                echo "PR_ID: ${env.PR_ID}"
                echo "PROJECT: ${env.PROJECT}"
                echo "ORIGINAL_JOB: ${env.ORIGINAL_JOB}"
                echo "TARGET_BRANCH: ${env.TARGET_BRANCH}"
            }
        }

        stage('Change version if part of PR') {
            when {
                expression {
                    env.BRANCH_NAME ==~ "PR-[0-9]+" || env.PR_ID ==~ "PR-[0-9]+"
                }
            }
            steps {
                script {
                    sh "mvn -s ${env.MVN_SETTINGS} versions:set -DnewVersion=${env.PROJECT_VERSION}"
                    echo "Changing MVN version to: ${env.PROJECT_VERSION}"
                }
            }
        }

        stage('Change dependencies') {
            when {
                expression {
                    env.PR_ID ==~ "PR-[0-9]+"
                }
            }
            steps {
                script {
                    sh "mvn -s ${env.MVN_SETTINGS} versions:use-dep-version -Dincludes=dk.kb.storage:* -DdepVersion=${env.PR_ID}-${env.ORIGINAL_JOB}-ds-storage-SNAPSHOT -DforceVersion=true"

                    echo "Changing MVN dependency storage to: ${env.PR_ID}-${env.ORIGINAL_JOB}-ds-storage-SNAPSHOT"
                }
            }
        }

        stage('Build') {
            when {
                expression {
                    env.BRANCH_NAME ==~ "master|release_v[0-9]+|PR-[0-9]+" || env.PR_ID ==~ "PR-[0-9]+"
                }
            }
            steps {
                script {
                    // Execute Maven build
                    sh "mvn -s ${env.MVN_SETTINGS} clean package"
                }
            }
        }

        stage('Push to Nexus') {
            when {
                // Check if Build was successful
                expression {
                    currentBuild.currentResult == "SUCCESS" && env.BRANCH_NAME ==~ "master|release_v[0-9]+|PR-[0-9]+|DRA-2011_Jenkins_build"
                }
            }
            steps {
                sh "mvn -s ${env.MVN_SETTINGS} clean deploy -DskipTests=true" // Kan vi skippe build let
            }
        }

        stage('Trigger Present Build') {
            when {
                expression {
                    currentBuild.currentResult == "SUCCESS" && (env.BRANCH_NAME ==~ "PR-[0-9]+" || env.PR_ID==~ "PR-[0-9]+")
                }
            }
            steps {
                script {
                    echo "Triggering: DS-GitHub/${env.BUILD_TO_TRIGGER}/${env.TARGET_BRANCH}"
                    def result = build job: "DS-GitHub/${env.BUILD_TO_TRIGGER}/${env.TARGET_BRANCH}",
                        parameters: [
                            string(name: 'PR_ID', value: env.BRANCH_NAME),
                            string(name: 'ORIGINAL_JOB', value: env.ORIGINAL_JOB),
                            string(name: 'TARGET_BRANCH', value: env.CHANGE_TARGET)
                        ]
                        wait: true // Wait for the pipeline to finish
                    echo "Child Pipeline Result: ${result}"
                }
            }
        }
    }
}