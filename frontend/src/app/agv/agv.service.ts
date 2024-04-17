import { Injectable, WritableSignal, effect, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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
  private httpClient: HttpClient = inject(HttpClient);
  private agvEvents$: WritableSignal<AgvEvent|null> = signal({
    UUUID: '9bf5ec17-afbc-4449-8a20-5e2b94a850de',
    id: 1,
    endpointUrl: 'localhost:8081/api/v1/agv/1',
    battery: 100,
    agvProgram: 'No program loaded',
    agvState: '1',
    name: 'Storeroom AGV'
  });

  constructor() { 
    this.subscribeToAgvSse();
    //this.fetchInitialAgvStatus();
  }

  private subscribeToAgvSse() {
    console.log('Subscribing to AGV SSE');
    const eventSource = new EventSource('http://localhost:8080/api/v1/agv/sse');
    eventSource.addEventListener("Agv Event", (e) => {
      this.agvEvents$.set(JSON.parse(e.data));
    })
  };

  public getAgvEvents$() {
    return this.agvEvents$.asReadonly();
  }

  /*
  private fetchInitialAgvStatus() {
    this.httpClient.get<AgvEvent>("http://localhost:8080/api/v1/agv/").subscribe((agvEvent) => {
      this.agvEvents$.set(agvEvent);
      console.log(agvEvent);
    });
  }
  */
}
