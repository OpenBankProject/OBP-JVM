# OBP-JVM
A set of libraries and a demo application.
*Not quite ready for release.* *Pull requests and comments very welcome*.

## Using this Library in your Project

Add dependencies

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC1-SNAPSHOT</version>
</dependency>
```
If you use Kafka, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-kafka</artifactId>
  <version>2016.11-RC1-SNAPSHOT</version>
</dependency>
```

For testing, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC1-SNAPSHOT</version>
  <type>test-jar</type>
  <scope>test</scope>
</dependency>
```

The library has not yet been released to maven central. To use it, clone the repository and run `mvn verify install`. 
This will install the library in your local repository.
