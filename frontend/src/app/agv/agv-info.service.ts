import { HttpClient } from '@angular/common/http';
import { Injectable, WritableSignal, inject, signal } from '@angular/core';
import { AgvLog } from './_models/agv-log';

@Injectable({
  providedIn: 'root'
})
export class AgvInfoService {
  private http: HttpClient = inject(HttpClient);
  private agvInfo$: WritableSignal<AgvLog|undefined> = signal(undefined);
  constructor() {
    this.getLastAgvInfo();
    this.subscribeToAgvInfo();
  }

  private subscribeToAgvInfo() {
    console.log('Subscribing to agv info');
    const eventSource = new EventSource('http://localhost:8080/api/v1/agv/agvsse');
    eventSource.addEventListener("agvEvent", (e) => {
      console.log("New agv info: ", JSON.parse(e.data));
      this.agvInfo$.set(JSON.parse(e.data));
    });
  }

  private getLastAgvInfo() {
    this.http.get<AgvLog>("http://localhost:8080/api/v1/agv/lastlog").subscribe((agvInfo) => {
      this.agvInfo$.set({name: agvInfo.agv?.name, battery: agvInfo.battery, agvProgram: agvInfo.agvProgram, agvState: agvInfo.agvState, endpointUrl: agvInfo.endpointUrl, id: agvInfo.id, uuid: agvInfo.uuid});
      console.log(this.agvInfo$());
    });
  }

  public getAgvInfo$() {
    return this.agvInfo$.asReadonly();
  }
}
