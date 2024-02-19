javac -d bin database/*.java
javac -d bin *.java
jar cfm app.jar MANIFEST.MF -C bin .