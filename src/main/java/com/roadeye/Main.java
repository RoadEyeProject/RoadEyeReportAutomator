package com.roadeye;

public class Main {
    public static void main(String[] args) {
        String redisHost = "localhost";
        int redisPort = 6379;
        String queueName = "event_queue";

        RedisQueueConsumer consumer = new RedisQueueConsumer(redisHost, redisPort, queueName);
        consumer.start();
    }
}
