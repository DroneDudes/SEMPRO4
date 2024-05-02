package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class AssemblyService {


    AssemblyConnection assemblyConnection;

    @Autowired
    public AssemblyService(AssemblyConnection assemblyConnection) {
        this.assemblyConnection = assemblyConnection;
    }

    public void startProduction(Blueprint blueprint, int amount) {
        //String status = assemblyConnection.subscribeToStatus();

        int[] productAmount = new int[amount];
        for (int i : productAmount) {
            System.out.println();
        }


    }

    public void stopProduction() {

    }


}
