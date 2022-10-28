# SaussiChaton

Command-line implementation of the Exploding Kittens game, made for the Software Engineering course.

## Requirements

- Java 11 or above
- Maven
- Python 3

## Running the game

First, you need to compile the code with maven :

```
mvn clean package
```

You can then run the server like so :

```
java -jar target/saussichaton-1.0.jar -n <2|3|4|5>
```

(Or you can just use IntellIJ to run the code)

And then you can run the client :

```
client/client.py -a <host>
```
