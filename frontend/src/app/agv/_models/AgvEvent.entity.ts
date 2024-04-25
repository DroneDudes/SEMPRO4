
export interface AgvEvent {
    UUUID: string;
    id: number;
    endpointUrl: string;
    battery: number;
    agvProgram: string;
    agvState: string;
    name: string;
  }