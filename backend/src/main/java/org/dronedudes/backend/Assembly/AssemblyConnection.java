package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.v3.core.util.Json;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    public String subscribe(String topic) {
        try {
            return client.subscribeWithResponse(topic).toString();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribeToStatus() {
        try {

            JSONObject jsonObject = new JSONObject();
            String status = (String) jsonObject.get("status");
            System.out.println(status);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}