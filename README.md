# kafka-forwarder
Forwards data from one topic (sourceTopic) to another topic (destinationTopic). The source or destination topic can be in another machine.
In this case the broker also need to be defined

Usage:

```bash
$ java -jar target/kafka-forwarder-1.0.0-selfcontained.jar
usage: java -jar kafka-forwarder.jar
    --sourceTopic <arg>         topic to read from (required)
    --destinationTopic <arg>    topic to produce to (required)
    --sourceBroker <arg>        broker of the source machine (default: localhost:9092)
    --destinationBroker <arg>   broker of the destination machine (default: localhost:9092)
    --help            
```

example :
 - java -jar kafka-forwarder-1.0.0-SNAPSHOT-selfcontained.jar --sourceTopic rb_state --destinationTopic rb_state --produceBroker rbmanager.redborder.cluster:9092

## Contributing

1. [Fork it](https://github.com/redborder/kafka-forwarder/fork)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request

## License

[AGPL v3](http://www.gnu.org/licenses/agpl-3.0.html)
