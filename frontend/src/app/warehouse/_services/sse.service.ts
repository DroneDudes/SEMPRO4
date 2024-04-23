import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SseService {
  constructor(private zone: NgZone) {}

  getEventSource(url: string): EventSource {
    return new EventSource(url);
  }

  getServerSentEvent(url: string): Observable<any> {
    return new Observable(observer => {
      const eventSource = this.getEventSource(url);

      eventSource.onmessage = event => {
        this.zone.run(() => {
          observer.next(event.data);
          console.log(event.data);
        });
      };

      eventSource.onerror = error => {
        this.zone.run(() => {
          observer.error(error);
        });
      };

      return () => {
        eventSource.close();
      };
    });
  }
}
