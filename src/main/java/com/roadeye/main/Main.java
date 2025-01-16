package com.roadeye.main;

import com.roadeye.automation.AutomationService;
import com.roadeye.consumer.RedisQueueConsumer;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        // Attributes for appium
        String serverUrl = "http://127.0.0.1:4723/";
        String deviceName = "pixel_9_emulator";
        String udid = "emulator-5554";
        String platformVersion = "15";
        // Attributes for Redis
        String redisHost = "localhost";
        int redisPort = 6379;
        String queueName = "event_queue";

        RedisQueueConsumer consumer = new RedisQueueConsumer(redisHost, redisPort, queueName);
        consumer.start();
    }
}
