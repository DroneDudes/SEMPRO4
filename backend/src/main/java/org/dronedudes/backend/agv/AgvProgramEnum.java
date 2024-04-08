package org.dronedudes.backend.agv;

import java.util.Arrays;
import java.util.Optional;

public enum AgvProgramEnum {
    NO_PROGRAM_LOADED("no program loaded", "No current program is loaded"),
    MOVE_TO_CHARGE("MoveToChargerOperation", "Move the AGV to the charging station"),
    MOVE_TO_ASSEMBLY("MoveToAssemblyOperation","Move the AGV to the assembly station"),
    MOVE_TO_STORAGE("MoveToStorageOperation", "Move the AGV to the warehouse"),
    PUT_IN_ASSEMBLY("PutAssemblyOperation", "Activate the robot arm to pick payload from AGV and place it at the assembly station"),
    PICK_FROM_ASSEMBLY("PickAssemblyOperation", "Activate the robot arm to pick payload at the assembly station and place it on the AGV"),
    PICK_FROM_WAREHOUSE("PickWarehouseOperation", "Activate the robot arm to pick payload from the warehouse outlet"),
    PUT_IN_WAREHOUSE("PutWarehouseOperation", "Activate the robot arm to place an item at the warehouse inlet");
    private final String name;
    private final String description;


     AgvProgramEnum(String name, String description) {
        this.description = description;
        this.name = name;
     }

    public static Optional<AgvProgramEnum> getAgvProgramByValue(String value) {
        return Arrays.stream(AgvProgramEnum.values())
                .filter(program -> program.name.equals(value)
                        || program.description.equals(value))
                .findFirst();
    }

    public String getDescription() {
         return this.description;
    }
    public String getName() {
         return this.name;
    }


}
