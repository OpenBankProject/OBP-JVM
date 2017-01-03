# OBP-JVM
A set of libraries and a demo application.
*Not quite ready for release.* *Pull requests and comments very welcome*.

## Using this Library in your Project

Add dependencies

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC5-SNAPSHOT</version>
</dependency>
```
If you use Kafka, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-kafka</artifactId>
  <version>2016.11-RC5-SNAPSHOT</version>
</dependency>
```

For testing, also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.11-RC5-SNAPSHOT</version>
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

Use the simple demo to check that it all works

```
java -cp obp-ri-demo/target/obp-ri-demo-2016.11-RC4-SNAPSHOT-jar-with-dependencies.jar com.tesobe.obp.demo.SimpleDemo
```
The SimpleDemo logs to `demo.log`. See `obp-ri-demo/src/main/resources/logback.xml`.

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

# Adding a method to the API

The new method will be using the existing **get** verb. 
What needs to done is to describe the data sent and received. To do that, define an interface in the package corresponding to the version you are supporting.

#### An Example: getChallengeThreshold

```scala
def getChallengeThreshold(userId: String, accountId: String, transactionRequestType: String, currency: String):(BigDecimal, String)
```

A method is needed that returns the amount and currency, as three letter ISO code. As the current version is *Nov2016*, define an interface in the package `com.tesobe.obp.transport.nov2016`:

```java
public interface ChallengeThreshold
{
  String amount();

  String currency();

  interface Parameters
  {
    String accountId = com.tesobe.obp.transport.nov2016.Parameters.accountId;
    String userId = com.tesobe.obp.transport.nov2016.Parameters.userId;
    String type = "type";
    String currency = "currency";
  }
}
```
The interface `ChallengeThreshold` specifies the fields that are returned, the subinterface `Parameters` specifies
the *names* of fields that are sent. A target must be added:

```java
  public enum Target
  {
    account, accounts, bank, banks, challengeThreshold, transaction,
    transactions, user, users
  }
```

Next the test case in `com.tesobe.obp.transport.spi.ConnectorNov2016Test`:

```java
@Test public void getChallengeThreshold() throws Exception
{
  Map<String, String> parameters = new HashMap<>();

  parameters.put(ChallengeThreshold.Parameters.accountId, "account-x");
  parameters.put(ChallengeThreshold.Parameters.userId, "user-x");
  parameters.put(ChallengeThreshold.Parameters.type, "type-x");
  parameters.put(ChallengeThreshold.Parameters.currency, "currency-x");

  Decoder.Response response = connector.get("getChallengeThreshold", Transport.Target.challengeThreshold, parameters);

  assertThat(response.error(), notPresent());
}
```
This will **not** fail. It is a legal request. The south side just does not know what to do with it and will return an empty result. This is the exchange with the encoding used by the version **Nov2016** (from the log):

```
TRACE 20:37:25.738 [main] c.t.o.t.spi.ConnectorNov2016 get:82 - fab8445f-7cb5-4483-b1b4-1af6e71788b6 {"accountId":"account-x","north":"getChallengeThreshold","name":"get","currency":"currency-x","type":"type-x","version":"Nov2016","userId":"user-x","target":"challengeThreshold"} → {"target":"challengeThreshold"}

```

This is the request: 

```{"accountId":"account-x","north":"getChallengeThreshold","name":"get","currency":"currency-x","type":"type-x","version":"Nov2016","userId":"user-x","target":"challengeThreshold"}```

This is the response:

```
{"target":"challengeThreshold"}
```

And `fab8445f-7cb5-4483-b1b4-1af6e71788b6 ` is the message id. When the test is extended to check the returned data, it will fail:

```java
    assertThat(response.error(), notPresent());
    assertThat(response.data().size(), is(1));
    assertThat(response.data().get(0).text(ChallengeThreshold.amount), is("amount-x"));
    assertThat(response.data().get(0).text(ChallengeThreshold.currency), is("currency-x"));
```
We expect one record in the result with two text fields. 
Some boilder-plate needs to be added to the interface to be able to use the field names in the result:

```java
public interface ChallengeThreshold
{
  String amount();

  String currency();

  default List<String> fields()
  {
    return FIELDS;
  }

  String amount = "amount";
  String currency = "currency";

  List<String> FIELDS = asList(amount, currency);

  interface Parameters
  {
    String accountId = com.tesobe.obp.transport.nov2016.Parameters.accountId;
    String userId = com.tesobe.obp.transport.nov2016.Parameters.userId;
    String type = "type";
    String currency = "currency";
  }
}
```

The values will be supplied by the mock responder, once the method is implemented on the south.

To make things easy for the user, define a reader for the data.

```java
public class ChallengeThresholdReader implements ChallengeThreshold
{
  public ChallengeThresholdReader(Data bank)
  {
    data = bank;
  }

  @Override public String amount()
  {
    return data.text(amount);
  }

  @Override public String currency()
  {
    return data.text(currency);
  }

  protected final Data data;
}
```
And the test can be re-written as:

```java
    assertThat(response.error(), notPresent());
    assertThat(response.data().size(), is(1));

    Optional<ChallengeThresholdReader> threshold = response.data()
      .stream()
      .map(ChallengeThresholdReader::new)
      .findFirst();

    assertThat(threshold, isPresent());
    assertThat(threshold.get().amount(), is("amount-x"));
    assertThat(threshold.get().currency(), is("currency-x"));
```

In Scala the result could be retrieved with:

```scala
response.data().map(data => new ChallengeThresholdReader(data)).headOption match {
  case Some(threshold) => ...
  case None => ...
}
```

Now the South. 
The `com.tesobe.obp.transport.spi.DefaultResponder` needs a new case for the new `Target` that was defined above.

```java
  case challengeThreshold:
    return challengeThreshold(state, p, ps);
```

And a new method in MockResponder to handle it.

```java
  @Override
  protected List<? extends Map<String, ?>> challengeThreshold(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    HashMap<String, String> response = new HashMap<>();

    response.put(ChallengeThreshold.amount, "amount-x");
    response.put(ChallengeThreshold.currency, "currency-x");

    return Collections.singletonList(response);
  }
```

Now the test now longer fails.

Finally, the new method needs to added to the supported methods in the **describe** call.
See `com.tesobe.obp.transport.spi.MockResponder#describe`.

