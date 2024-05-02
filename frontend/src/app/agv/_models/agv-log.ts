export interface AgvLog {
    agv?: Agv;
    agvProgram: string;
    agvState: string;
    battery: number;
    endpointUrl: string;
    id: number;
    name: string | undefined;
    uuid: string;
}

export interface Agv {
    uuid: string
    id: number
    endpointUrl: string
    battery: number
    agvProgram: any
    agvState: any
    name: string
  }
