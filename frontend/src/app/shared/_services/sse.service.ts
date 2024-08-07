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
    this.getLast10Logs();
    this.subscribeToSse();
  }

  
  private subscribeToSse() {
    const eventSource = new EventSource('http://localhost:8080/api/v1/sse/log');
    eventSource.addEventListener("LogEntry", (e) => {
      this.sseLogs$().unshift(JSON.parse(e.data));
      this.sseLogs$().splice(10);
    })
  };
  


  private getLast10Logs() {
    this.httpClient.get<SseLog[]>("http://localhost:8080/api/v1/logs/last10logs").subscribe((sseLogs) => {
      this.sseLogs$.set(sseLogs);
    });
  }

  public getLogs$() {
    return this.sseLogs$.asReadonly();
  }
}
