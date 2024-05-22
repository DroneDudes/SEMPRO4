import { Injectable, WritableSignal, signal } from '@angular/core';
import { AssemblyStation } from './assembly';
@Injectable({
  providedIn: 'root'
})
export class AssemblyserviceService {
private assemblyInfo$: WritableSignal<AssemblyStation|undefined> = signal(undefined);
  constructor() { 
    this.subscribeToAssemblyInfo();
  }

  private subscribeToAssemblyInfo() {
    console.log('Subscribing to assembly info');
    const eventSource = new EventSource('http://localhost:8080/api/v1/assembly/assemblysse');
    eventSource.addEventListener("assemblyEvent", (e) => {
      console.log("New assembly info: ", JSON.parse(e.data));
      this.assemblyInfo$.set(JSON.parse(e.data));
    });
  }

  public getAssemblyInfo$() {
    return this.assemblyInfo$.asReadonly();
  }
}
