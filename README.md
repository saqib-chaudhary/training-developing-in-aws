### Java AWS SDK

https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html


```
# missing maven on Amazon linux 2

cd /tmp
sudo wget https://downloads.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz
sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt

# find alternatives to find java path 
sudo alternatives --config java

# or

readlink -f $(which java) | sed 's/\/bin\/java//'

sudo nano /etc/profile.d/maven.sh


export JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto.x86_64
export M2_HOME=/opt/apache-maven-3.9.1
export MAVEN_HOME=/opt/apache-maven-3.9.1
export PATH=${M2_HOME}/bin:${PATH}

sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh

# Directly executing App with lib in path 
mvn clean compile exec:exec

# Using fat jar to it 
mvn clean compile assembly:single
java -jar target/aws-training-1.0-SNAPSHOT-jar-with-dependencies.jar tpicap us-east-2 

```

**Create Fat (Uber) Maven jar with all dependencies** 

https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-runnable-jar-with-dependencies-using-maven

