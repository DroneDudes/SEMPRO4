
  
  export interface AgvLog {
    id: number
    battery: number
    agvProgram: string
    agvState: number
    timestamp: string
    agv: Agv
    userId: number
    carriedPartId: number
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