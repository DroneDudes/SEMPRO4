import { Injectable, WritableSignal, effect, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SseLog } from '../_models/sse-log';


@Injectable({
  providedIn: 'root'
})
export class SseService {
  private httpClient: HttpClient = inject(HttpClient);
  private sseLogs$: WritableSignal<SseLog[]> = signal([]);

  constructor() { 
    //this.subscribeToSse();
    this.getLast10Logs();
  }

  /*
  private subscribeToSse() {
    console.log('Subscribing to AGV SSE');
    const eventSource = new EventSource('http://localhost:8080/api/v1/agv/sse');
    eventSource.addEventListener("Agv Event", (e) => {
      this.agvEvents$.set(JSON.parse(e.data));
      this.getLast10Logs();
    })
  };
  */


  private getLast10Logs() {
    console.log('Getting last 10 logs');
    this.httpClient.get<SseLog[]>("http://localhost:8080/api/v1/logs/last10logs").subscribe((sseLogs) => {
      this.sseLogs$.set(sseLogs);
      console.table(this.sseLogs$());
    });
  }

  public getLogs$() {
    return this.sseLogs$.asReadonly();
  }
}
