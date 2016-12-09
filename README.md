# OBP-JVM
A set of libraries and a demo application.
*Not quite ready for release.* *Pull requests and comments very welcome*.

## Using this Library in your Project

Add dependencies

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC4-SNAPSHOT</version>
</dependency>
```
If you use Kafka, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-kafka</artifactId>
  <version>2016.11-RC4-SNAPSHOT</version>
</dependency>
```

For testing, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC4-SNAPSHOT</version>
  <type>test-jar</type>
  <scope>test</scope>
</dependency>
```

The library has not yet been released to maven central. To use it, clone the repository and run `mvn verify install`. 
This will install the library in your local repository. In detail, do this:

```
$ git clone https://github.com/OpenBankProject/OBP-JVM.git
$ cd OBP-JVM
$ git checkout nov2016
$ mvn clean verify install
```

This will install the current development branch into your local maven repository.
To check do `find ~/.m2/repository -name obp-ri\*.jar`.

Whenever you need to update, change to the branch you want to use, pull, and `mvn clean install`.

## Run the kafka demo

Install and start kafka. The demo needs two topics that default to **Request** amd **Response**.

```
$ java -jar obp-ri-demo/target/obp-ri-demo-2016.11-RC4-SNAPSHOT-jar-with-dependencies.jar --help
Option                             Description                                 
------                             -----------                                 
-?, -h, --help                     This message.                               
--consumer-props <String: CPROPS>  Consumer configuration (default: consumer.  
                                     props)                                    
--consumer-topic <String: CTOP>    Consumer topic (default: south Request,     
                                     north Response)                           
--kafka                            Use Apache Kafka                            
--north                            This is the north side                      
--producer-props <String: PPROPS>  Producer configuration (default: producer.  
                                     props)                                    
--producer-topic <String: PTOP>    Producer topic. (default: north Request,    
                                     south Response)                           
--responder <String: RESPONDER>    The demo data (default: com.tesobe.obp.demo.
                                     OneBankTwoAccounts)                       
--south                            This is the south side                          
```

If you can use the default configuration for kafka you can then start the south and north sides of the demo.
They can by started in any order but if you wish to start them in a single console this way is clearer:

```
$ java -jar obp-ri-demo/target/obp-ri-demo-2016.11-RC4-SNAPSHOT-jar-with-dependencies.jar --kafka --south&
Starting TESOBE's OBP kafka south demo...
Check the log files 'demo.log' and 'south.log'
```
This process does not write to the console so it is ok to start the north side in the same console. 
North will send a few requests to the south, print the response and then exit. 
The details are in **demo.log**. 
The JSON messages only are in **south.log**

```
$ java -jar obp-ri-demo/target/obp-ri-demo-2016.11-RC4-SNAPSHOT-jar-with-dependencies.jar --kafka --north
Starting TESOBE's OBP kafka south demo...
Check the log files 'demo.log' and 'south.log'
Starting TESOBE's OBP kafka north demo...
Check the log files 'demo.log' and 'south.log'
working...
The banks (1)
  fo First Open
The users (3)
  a Анна
  b Berta
  c 金色
Анна's accounts at First Open (1)
  1 fo a
Berta's accounts at First Open (2)
  2 fo b
  3 fo b
金色's accounts at First Open (0)
done.
```
```
$ less south.log
INFO  00:11:24.347 [pool-1-thread-1] 5377acaf-2a37-4a48-bebc-48da3265e3a1 → {"north":"getBanks","name":"get","count":0,"version":"Nov2016","target":"banks"}
INFO  00:11:24.356 [pool-1-thread-1] 5377acaf-2a37-4a48-bebc-48da3265e3a1 ← {"data":[{"bankId":"fo","name":"First Open","logo":"http://www.example.com/logo","url":"http://www.example.com/"}],"count":0,"target":"banks"}
INFO  00:11:27.515 [pool-1-thread-1] 4845b807-029b-4d28-8caf-67b4b3bc21a4 → {"north":"getUsers","name":"get","count":0,"version":"Nov2016","target":"users"}
INFO  00:11:27.515 [pool-1-thread-1] 4845b807-029b-4d28-8caf-67b4b3bc21a4 ← {"data":[{"name":"Анна","id":"a","email":"anna@example.com"},{"name":"Berta","id":"b","email":"berta@example.com"},{"name":"金色","id":"c","email":"chin@example.com"}],"count":0,"target":"users"}
INFO  00:11:28.039 [pool-1-thread-1] 451bae36-d0ff-4239-9f4a-7a6f62e6fd19 → {"bankId":"fo","north":"getAccounts","name":"get","count":0,"version":"Nov2016","userId":"a","target":"accounts"}
INFO  00:11:28.050 [pool-1-thread-1] 451bae36-d0ff-4239-9f4a-7a6f62e6fd19 ← {"data":[{"accountId":"1","number":"FO-A1","bankId":"fo","balanceAmount":"42","balanceCurrency":"RUB","userId":"a"}],"count":0,"target":"accounts"}
INFO  00:11:28.610 [pool-1-thread-1] a0c70f06-a052-4537-815f-b09abdf8b815 → {"bankId":"fo","north":"getAccounts","name":"get","count":0,"version":"Nov2016","userId":"b","target":"accounts"}
INFO  00:11:28.610 [pool-1-thread-1] a0c70f06-a052-4537-815f-b09abdf8b815 ← {"data":[{"accountId":"2","number":"FO-B1","bankId":"fo","balanceAmount":"42","balanceCurrency":"EUR","userId":"b"},{"accountId":"3","number":"FO-B2","bankId":"fo","balanceAmount":"42","balanceCurrency":"USD","userId":"b"}],"count":0,"target":"accounts"}
INFO  00:11:29.150 [pool-1-thread-1] ee66fd3d-c1e3-43e3-bd7f-dfd7debcd5fe → {"bankId":"fo","north":"getAccounts","name":"get","count":0,"version":"Nov2016","userId":"c","target":"accounts"}
INFO  00:11:29.150 [pool-1-thread-1] ee66fd3d-c1e3-43e3-bd7f-dfd7debcd5fe ← {"count":0,"target":"accounts"}
```




