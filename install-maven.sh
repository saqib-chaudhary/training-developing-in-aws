
cd /tmp
sudo wget https://downloads.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz
sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt
sudo echo "export JAVA_HOME="`readlink -f $(which java) | sed 's/\/bin\/java//'` > /etc/profile.d/maven.sh
sudo echo "export M2_HOME=/opt/apache-maven-3.9.1" >> /etc/profile.d/maven.sh
sudo echo "export MAVEN_HOME=/opt/apache-maven-3.9.1" >> /etc/profile.d/maven.sh
sudo echo 'export PATH=${M2_HOME}/bin:${PATH}' >> /etc/profile.d/maven.sh
sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh
mkdir -p ../.c9/runners && cp AppRunner.run ../.c9/runners
