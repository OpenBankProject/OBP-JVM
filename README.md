# OBP-JVM
A set of libraries and a demo application.
*Not quite ready for release.*

## Using this Library in your Project

Add a dependency

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-kafka</artifactId>
  <version>2016.0-SNAPSHOT</version>
</dependency>
```
For testing also add

```xml
<dependency>
  <groupId>com.tesobe.obp</groupId>
  <artifactId>obp-ri-transport</artifactId>
  <version>2016.0-SNAPSHOT</version>
  <classifier>tests</classifier>
</dependency>
```

### Implementing the South
To build a connector that is called by OBP-API you need to do three things:

  * Implement a version of the service provider interface
  * Pick an encoding (or implement your own)
  * Pick a transport (or implement your own) 

#### Implementing the SPI

If you want _(but there is no need to, see below)_ to do everything from scratch, all you need to implement is `com.tesobe.obp.transport.spi.Receiver`. It has one method:

```java
public interface Receiver
{
  String respond(Message request);
}
```

The request has an **id** and a **payload**.
The **id** is a `UUID` used to match the response to the request.
The **payload** is a request by OBP-API encoded in the encoding you choose, or that was choosen for you. 

The possible requests depend on the version of the SPI you are using.

For the `Version.legacy` the possible requests are listed in `com.tesobe.obp.transport.spi.LegacyResponder`. 
The **LegacyResponder** implements **Receiver** and has an abstract method for each request.

You need to subclass **LegacyResponder** and implement these methods. This is an example for one of them:

```java
protected abstract String getPrivateBank(String payload, Decoder.Request r, Encoder e);
```

The first argument **payload** is the verbatim request as taken from the message.
It is not needed for the response but it has one important use: 
Pass it on to the APIs you are calling to document the origin of the call.

The second argument, of type **Decoder.Request** is used to decode the request.

The third argument, of type **Encode** is used to encode the response.

Every request is implemented by `com.tesobe.obp.transport.spi.MockLegacyResponder` used to test
the **LegacyResonder**. The Demo gives a more complete example: `com.tesobe.obp.demo.south.DemoData`.
A third test worth looking at because it implements the **North** and **South** sides is: `com.tesobe.obp.transport.spi.LegacyConnectorTest`.

To get you started, you may use `com.tesobe.obp.transport.spi.DefaultLegacyResponder`.
It has a no-op implementation of every method in LegacyResponder.

### Implementing the North

(Look at `com.tesobe.obp.demo.SuperSimpleDemo` while you are reading this chapter)

The connector `com.tesobe.obp.transport.Connector` lists the methods you can call to communicate with external parties, the south. 
To optain a connector you need to implement `com.tesobe.obp.transport.Sender` and:

```java
import com.tesobe.obp.transport.Transport;

Transport.Factory factory = Transport.defaultFactory();
Decoder decoder = factory.decoder();
Encoder encoder = factory.encoder();
MySender sender = MySender(decoder, encoder);
Connector connector = factory.connector(sender);
```

The sender send messages to the south and receives the responses.
If the south is local to the north, the sender only has one method to call on **MyResponder**, an implementation of `com.tesobe.obp.transport.spi.Receiver`:

```java
MyResponder responder = new MyResponder(decoder, encoder);
Sender = request -> responder.respond(request);
```

For a simple but complete implementation of **Receiver** for testing see `com.tesobe.obp.transport.spi.MockLegacyResponder`.

Now, to use the **connector** simply call the methods, for example:

```java
String bankId = "...";
String userId = "...";

Optional<Bank> bank = connector.getPrivateBank(bankId, userId);
```

Examples for all methods in the connector are here: `com.tesobe.obp.transport.spi.LegacyConnectorTest `.

### Implementing Encoders and Decoders

### Implementing a Transport

### Implementing a new Version of the API / SPI


## Design

This is a redesign of
[OBP-API project's](https://github.com/OpenBankProject/OBP-API) `code.bankconnectors.Connector`.
The connector is used to communicate with extensions/plugins/backends that are
independently developed.

The goals are:

  * Support multiple versions of the API
  * Pluggable encoding of messages (defaulting to JSON)
  * Pluggable transports
    * Kafka
    * In memory (directly linked to OBP-API)
  * Easy testing
  * **Clearly label public and private data**
  * **Do not pass user data to anonymous methods**
  * Provide a demo

**Your feedback is very much appreciated**

Currently, the Kafka implementation is very simple, allowing only a single consumer and producer. 
The response must arrive before a the next request can be made.
My knowledge of Kafka is too limited to implement a higher throughput version.

## Kafka

We are using Kafka version **0.10.0.0**.
Start the servers and add the topics configured in your copy of OBP-API.
The names of the topics are specified in `OBP-API/src/main/resources/props/default.props`.
Substitute the name of your `.props` file. By default they are _Request_ and _Response_.

If there is no props file in `OBP-API/src/main/resources/props/`, then `default.props` copied from `sample.props.template` will do.
In the `.props` file these lines are required:

```
connector=kafka
kafka.zookeeper_host=localhost:2181
kafka.request_topic=Request
kafka.response_topic=Response
```

Create the topics

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Request
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Response
```

For testing reduce the retention time to, for example, one minute

```
bin/kafka-configs.sh --zookeeper localhost:2181 --entity-type topics --alter --add-config retention.ms=1000 --entity-name Request
bin/kafka-configs.sh --zookeeper localhost:2181 --entity-type topics --alter --add-config retention.ms=1000 --entity-name Response
```

Then start the server

```
cd OBP_API
mvn jetty:run
```

These are the default property files use by the south side demo:

```
obp-ri-demo/src/main/resources/com/tesobe/obp/demo/south/consumer.props
obp-ri-demo/src/main/resources/com/tesobe/obp/demo/south/producer.props
```

Command line flags for the south demo

```
java -jar obp-ri-demo/target/obp-ri-demo-2016.0-SNAPSHOT-jar-with-dependencies.jar -h
Option                             Description
------                             -----------
-?, -h, --help                     This message.
-c, --consumer <CONSUMER>          Consumer Configuration (default: consumer.
                                     props)
--consumer-topic <CONSUMER_TOPIC>  Consumer Topic (default: Request)
-p, --producer <PRODUCER>          Producer Configuration (default: producer.
                                     props)
--producer-topic <PRODUCER_TOPIC>  Producer Topic (default: Response)
```

Start the south demo

```
java -jar obp-ri-demo/target/obp-ri-demo-2016.0-SNAPSHOT-jar-with-dependencies.jar&
```

Try a request

```
http://localhost:8080/obp/v2.0.0/banks
```

Checking compatability with OBP-API with `KafkaMappedConnector.scala`

```
curl -v -H "Accept:application/json" -H "Content-Type:application/json" -X GET http://localhost:8080/obp/v2.0.0/bank
curl -v -H "Accept:application/json" -H "Content-Type:application/json" -X GET http://localhost:8080/obp/v2.0.0/banks

```
