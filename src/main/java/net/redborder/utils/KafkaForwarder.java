package net.redborder.utils;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.redborder.utils.consumers.*;
import net.redborder.utils.producers.*;

public class KafkaForwarder {
    private final SimpleKafkaConsumer consumer;
    private final SimpleKafkaProducer producer;
    private volatile boolean running = true;
    public static Logger log = LoggerFactory.getLogger(KafkaForwarder.class);

    public KafkaForwarder(String consumerBroker, String producerBroker, String sourceTopic, String destinationTopic, String groupId) {
        this.consumer = new SimpleKafkaConsumer(consumerBroker, groupId, sourceTopic);
        this.producer = new SimpleKafkaProducer(producerBroker, destinationTopic);
    }

    public void start() {
        // Start the consumer in a new thread
        Thread thread = new Thread(() -> {
            consumer.start(record -> {
                // For each message consumed, forward it to the destination topic
                String key = record.key();
                String value = record.value();
                log.info("Forwarding message with key: {}, value: {}", key, value);
                producer.send(key, value);
            });
        });
        thread.start();
    }

    public void stop() {
        running = false;
        consumer.stop();
        producer.close();
    }

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(null, "sourceTopic", true, "topic to read from (required)");
        options.addOption(null, "sourceBroker", true, "broker of the source machine (default: localhost:9092)");
        options.addOption(null, "destinationTopic", true, "topic to produce to (required)");
        options.addOption(null, "destinationBroker", true, "broker of the destination machine (default: localhost:9092)");
        options.addOption(null, "help", false, "show this help");

        CommandLineParser parser = new BasicParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine cmdLine = null;

        try {
            cmdLine = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("One or more of the options specified are not allowed. Check the options and try again.");
            System.exit(1);
        }

        if (cmdLine.hasOption("help") || !cmdLine.hasOption("sourceTopic") || !cmdLine.hasOption("destinationTopic")) {
            helpFormatter.printHelp("java -jar kafka-forwarder.jar", options);
            System.exit(1);
        }

        String consumerBroker = String.valueOf(cmdLine.getOptionValue("sourceBroker", "localhost:9092"));
        String producerBroker = String.valueOf(cmdLine.getOptionValue("destinationBroker", "localhost:9092"));
        String sourceTopic = String.valueOf(cmdLine.getOptionValue("sourceTopic"));
        String destinationTopic = String.valueOf(cmdLine.getOptionValue("destinationTopic"));
        String groupId = "kafka-forwarder-group";

        KafkaForwarder forwarder = new KafkaForwarder(consumerBroker, producerBroker, sourceTopic, destinationTopic, groupId);
        forwarder.start();

        // Add shutdown hook to gracefully stop the forwarder
        Runtime.getRuntime().addShutdownHook(new Thread(forwarder::stop));
    }
}
