package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;

public class AssemblyConnection {

    private MqttClient client;
    private ObjectMapper mapper = new ObjectMapper();
    private MqttMessage message = new MqttMessage();
    private final String brokerURL = "tcp://localhost:1883";

    public AssemblyConnection() {
        try {
            client = new MqttClient(brokerURL, MqttClient.generateClientId());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to MQTT broker lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Message arrived");
                    System.out.println("Topic: " + topic + " | Payload: " + message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete");
                }
            });

            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, Process payload) {
        try {
            String jsonPayload = mapper.writeValueAsString(payload);
            message.setPayload(jsonPayload.getBytes());
            client.publish(topic, message);
        } catch (JsonProcessingException | MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}