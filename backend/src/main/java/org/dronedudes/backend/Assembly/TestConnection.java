package org.dronedudes.backend.Assembly;

public class TestConnection {
    public static void main(String[] args) {
        AssemblyConnection connection = new AssemblyConnection();
        String topic = "emulator/operation";
        connection.subscribe(topic);

        Process process = new Process(12345);
        connection.publish(topic,process);
    }
}
