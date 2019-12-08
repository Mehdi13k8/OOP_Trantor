##
## EPITECH PROJECT, 2018
## make
## File description:
## make
##

all: package
	rm -rf dist
	mkdir dist
	cp target/*.jar dist/.
	chmod +x dist/*

clean:
	mvn clean
	rm -rf dist/*.jar

package:
	mvn package

build:
	mvn compile

test:
	mvn test

deps:
	mvn dependency:resolve
run:
	mvn exec:java -Dexec.mainClass="hello.Trantor"

runflag:
	mvn exec:java -Dexec.mainClass="Trantor" -Dexec.args="-p 2323 -x 232 -y 55 -n team1 team2 epic2 -c 23 -f 42"

fclean: clean
	rm -f *#
	rm -f *~
	rm -f src/main/java/hello/*.class

re: clean all
