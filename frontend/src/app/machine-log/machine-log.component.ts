import { Component, Signal, inject } from '@angular/core';
import { AgvLog, AgvService } from '../shared/_services/sse.service';
@Component({
  selector: 'app-machine-log',
  standalone: true,
  imports: [],
  templateUrl: './machine-log.component.html',
  styleUrl: './machine-log.component.css'
})
export class MachineLogComponent {
  private agvService: AgvService = inject(AgvService);
  public agvLogs$: Signal<AgvLog[]|null> = this.agvService.getAgvLogs$();
}