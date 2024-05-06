package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class AssemblyService {


    private AssemblyConnection assemblyConnection;

    @Autowired
    public AssemblyService(AssemblyConnection assemblyConnection) {
        this.assemblyConnection = assemblyConnection;
    }

    public void startProduction() {
        assemblyConnection.publish("emulator/operation", new Process(12345));
        }


}
