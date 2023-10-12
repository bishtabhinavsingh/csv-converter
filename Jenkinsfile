node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'Default Maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify mvn clean verify sonar:sonar \
                                        -Dsonar.projectKey=csv-converter \
                                        -Dsonar.projectName='csv-converter' \
                                        -Dsonar.host.url=http://10.0.0.138:9000 \
                                        -Dsonar.token=sqp_9b1251f0bd7d74625c360e7a5e6a7cdc1b2e922d"
    }
  }
}