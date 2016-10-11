```
             North                   South
             
             OBP API                 Bespoke Extension
               |                       |
obp-jvm      Connector                 |
             Encoder    <- JSON ->   Decoder
             Sender                  Receiver
               \                      /
transport     Kafka Sender         Kafka Receiver
           or In Memory Sender  or In Memory Receiver
           or ...
```
