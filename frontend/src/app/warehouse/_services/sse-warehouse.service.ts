import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Warehouse } from '../_models/Warehouse';

@Injectable({
  providedIn: 'root'
})
export class SseWarehouseService {
  
  private eventSource: EventSource | null = null;
  private sseDataSubject: Subject<Warehouse[]> = new Subject<Warehouse[]>();
  constructor(private httpClient: HttpClient) { }

  private connectToSSE() {
    this.eventSource = new EventSource('http://localhost:8080/sse/v1/warehouses');
    this.eventSource.onmessage = (event) => {
      this.sseDataSubject.next(event.data);
    };
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
      this.eventSource?.close();
    };
  }

  subsribeToWarehouseSse(): Observable<Warehouse[]> {
    if (!this.eventSource) {
      this.connectToSSE();
    }
    return this.sseDataSubject.asObservable();
  }

}
