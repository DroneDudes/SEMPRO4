package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Configuration
public class AssemblyConnection {

    private MqttClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final MqttMessage message = new MqttMessage();
    private final String brokerURL = "tcp://localhost:9001";

    private AssemblyStation assemblyStation = new AssemblyStation(0);

    public AssemblyConnection() {
        try {
            client = new MqttClient(brokerURL, MqttClient.generateClientId(),null);
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

    public String subscribe(String topic) {
        try {
            return client.subscribeWithResponse(topic).toString();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }


    public void subscribeToState() {
        try {
            client.subscribeWithResponse("emulator/status", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    byte[] payload = message.getPayload();
                    String payloadString = new String(payload, StandardCharsets.UTF_8);
                    JsonNode jsonPayload = mapper.readTree(payloadString);
                    String state = String.valueOf(jsonPayload.get("State"));
                    assemblyStation.setState(Integer.parseInt(state));
                }
            });
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void printState(){
        try {
            while (true) {
                Thread.sleep(3000);
                System.out.println(assemblyStation.getState());
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}