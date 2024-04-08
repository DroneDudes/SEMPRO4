//package org.dronedudes.backend.agv.program;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//import org.dronedudes.backend.agv.AgvLogEntry;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity(name = "agv_program")
//public class AgvProgram {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @JsonIgnore
//    @ToString.Exclude
//    @OneToMany(mappedBy = "agv_program", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<AgvLogEntry> agvLogsEntries;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String description;
//
//    public AgvProgram(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
//}