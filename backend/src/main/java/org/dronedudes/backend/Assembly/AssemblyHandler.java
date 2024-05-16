//package org.dronedudes.backend.Assembly;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.transaction.Transactional;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.dronedudes.backend.Assembly.log.AssemblyLogEntry;
//import org.dronedudes.backend.Assembly.log.AssemblyLogEntryService;
//import org.dronedudes.backend.Assembly.sse.AssemblySseService;
//import org.dronedudes.backend.common.ObserverService;
//import org.dronedudes.backend.common.PublisherInterface;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//
//
//public class AssemblyHandler implements PublisherInterface {
//
//
//
//
//
//
//
//
////    @Scheduled(fixedDelay = 1000)
////    public void logAssemblyData() {
////        currentState = Integer.parseInt(assemblyConnection.getState());
////        operationId = Integer.parseInt(assemblyConnection.getCurrentOperation());
////
////        if (currentState != priorState) {
////            Optional<AssemblyStation> optionalAssemblyStation = assemblyService.getAssemblyStationById(1L);
////            AssemblyStation assemblyStation = optionalAssemblyStation.orElseThrow(() -> new RuntimeException("AssemblyStation not found"));
////            AssemblyLogEntry assemblyLogEntry = new AssemblyLogEntry(operationId, currentState, assemblyStation);
////            assemblyLogService.save(assemblyLogEntry);
////            assemblySseService.updateSse(assemblyLogEntry);
////            priorState = currentState;
////        }
////    }
//
//
//}

