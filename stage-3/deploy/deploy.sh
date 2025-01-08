ssh -p 2222 s367527@helios.cs.ifmo.ru 'cd ~/is/is-cw/stage-3; git pull; mvn clean install; java -XX:MaxHeapSize=1G -XX:MaxMetaspaceSize=128m -jar target/parapp-0.0.1-SNAPSHOT.jar'
ssh -L 18124:localhost:18125 -p 2222 s367527@helios.cs.ifmo.ru
