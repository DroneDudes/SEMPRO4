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
  private agvEvents$: WritableSignal<AgvEvent|null> = signal(null);

  constructor() { 
    this.subscribeToAgvSse();
    this.fetchInitialAgvStatus();
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

  private fetchInitialAgvStatus() {
    this.httpClient.get<AgvEvent>("http://localhost:8080/api/v1/agv/").subscribe((agvEvent) => {
      this.agvEvents$.set(agvEvent);
      console.log(agvEvent);
    });
  }
}
