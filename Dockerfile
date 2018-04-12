FROM tomcat:8.5.29-jre8

#COPY ./tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

COPY ./build/libs/cph.glazier-assignment-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/myapp.war
