import { Injectable, WritableSignal, effect, signal } from '@angular/core';

export interface AgvEvent {
  UUUID: string;
  id: number;
  endpointUrl: string;
  battery: number;
  agvProgram: string;
  agvState: string;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class AgvService {

  private agvEvents$: WritableSignal<AgvEvent|null> = signal(null);

  constructor() { 
    this.subscribeToAgvSse();
  }

  private subscribeToAgvSse() {
    console.log('Subscribing to AGV SSE');
    const eventSource = new EventSource('http://localhost:8080/api/v1/agv/sse');
    eventSource.addEventListener("Agv Event", (e) => {
      this.agvEvents$.set(JSON.parse(e.data));
    })
  };

  getAgvEvents$() {
    return this.agvEvents$.asReadonly();
  }
}
