FROM tomcat:9.0.85-jre21

COPY tomcatConfigs/ /usr/local/tomcat/conf
COPY target/ /usr/local/tomcat/webapps

EXPOSE 8443
