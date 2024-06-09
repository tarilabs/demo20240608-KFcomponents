FROM eclipse-temurin:21

RUN mkdir /app/
RUN mkdir /app/lib

COPY ./myCamel.jar /app/myCamel.jar
COPY ./lib/* /app/lib/

WORKDIR /app

CMD "java" "-jar" "myCamel.jar" "/data"
