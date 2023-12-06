FROM openjdk:21-jdk
LABEL authors="Martin"
WORKDIR APP/
COPY build/libs/SocialNet-0.0.1-SNAPSHOT.jar app.jar

# Install wait-for-it
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh


# Wait for the database to be ready
CMD ["/app/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]

#CMD [ "java", "-jar", "app.jar" ]