cd server/
mkdir -pv plugins
cd plugins/
cp ../../target/MusicPlayer-*.jar MusicPlayer.jar
wget -nc https://cdn.modrinth.com/data/1bZhdhsH/versions/SKgeYMeH/PlasmoVoice-Paper-2.1.8.jar
cd ..
java -Xmx2048M -jar paper-1.21.1-133.jar
