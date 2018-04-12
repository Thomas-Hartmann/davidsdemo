#!/bin/bash
#
# Import config variables
source ./config

#####################
### Init services ###
#####################
# Stop and remove jenkinsci container
docker stop jenkinsci
docker rm jenkinsci

# Start and run new container instance
docker run \
  -d \
  --name jenkinsci \
  --group-add $(getent group docker | cut -d: -f3) \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  jenkinsci/blueocean:1.4.2

######################
### Jenkins config ###
######################
# Wait for service
echo "Waiting for jenkins admin password..."
while ! ADMIN_PASSWORD=$(docker exec jenkinsci cat /var/jenkins_home/secrets/initialAdminPassword 2>&1); do
  sleep 1
done

# Wait for service
echo "Waiting for jenkins web API..."
while ! curl -i -s -u "admin:$ADMIN_PASSWORD" http://localhost:8080/credentials/store/system/domain/_/ | grep "200" > /dev/null; do
  sleep 1
done

# Get crumb value
CRUMB=$(curl -u "admin:$ADMIN_PASSWORD" 'http://localhost:8080/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,":",//crumb)' | sed 's/.*Jenkins-Crumb://')

# Add github api-key
curl -i -X POST -u "admin:$ADMIN_PASSWORD" 'http://localhost:8080/credentials/store/system/domain/_/createCredentials' \
--header "Jenkins-Crumb:$CRUMB" \
--data-urlencode "json={
   '':'0',
   'credentials':{
      'scope':'GLOBAL',
      'username':'$GITHUB_USER',
      'password':'$GITHUB_API_KEY',
      'id':'github-api-token',
      'description':'github api token',
      'stapler-class':'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
      '\$class':'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl'
   }
}"

# Setup temporary jenkins job xml
cp job-config.xml job-config-tmp.xml

# Add job configurations
sed -i "s/<repoOwner>.*<\/repoOwner>/<repoOwner>$GITHUB_USER<\/repoOwner>/g" job-config-tmp.xml
sed -i "s/<repository>.*<\/repository>/<repository>$GITHUB_REPO<\/repository>/g" job-config-tmp.xml

# Add job
curl -i -X POST -u "admin:$ADMIN_PASSWORD" "http://localhost:8080/createItem?name=$GITHUB_REPO" \
--header "Jenkins-Crumb:$CRUMB" \
--header "Content-Type:text/xml" \
--data-binary @job-config-tmp.xml

# Remove temporary jenkins job xml
rm job-config-tmp.xml

echo "Service is ready!"
echo "Jenkins admin password: $ADMIN_PASSWORD"
