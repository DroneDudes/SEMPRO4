import { Injectable, WritableSignal, effect, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SseLog } from '../_models/sse-log';


@Injectable({
  providedIn: 'root'
})
export class SseService {
  private httpClient: HttpClient = inject(HttpClient);
  private agvEvents$: WritableSignal<SseLog|null> = signal({
    timestamp: "2024-04-25 10:30:35",
    machine_name: "Agv 1",
    action: "MoveToAssembly"
  });

  private sseLogs$: WritableSignal<SseLog[]> = signal([]);

  constructor() { 
    this.subscribeToSse();
    this.getLast10Logs();
  }

  private subscribeToSse() {
    console.log('Subscribing to AGV SSE');
    const eventSource = new EventSource('http://localhost:8080/api/v1/agv/sse');
    eventSource.addEventListener("Agv Event", (e) => {
      this.agvEvents$.set(JSON.parse(e.data));
      this.getLast10Logs();
    })
  };


  private getLast10Logs() {
    this.httpClient.get<SseLog[]>("http://localhost:8080/api/v1/agv/logs").subscribe((sseLogs) => {
      this.sseLogs$.set(sseLogs);
      console.table(this.sseLogs$());
    });
  }

  /*
  private fetchInitialAgvStatus() {
    this.httpClient.get<AgvEvent>("http://localhost:8080/api/v1/agv/").subscribe((agvEvent) => {
      this.agvEvents$.set(agvEvent);
      console.log(agvEvent);
    });
  }
  */
  public getLogs$() {
    return this.sseLogs$.asReadonly();
  }
}
