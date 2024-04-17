import { Component, Signal, inject } from '@angular/core';
import { AgvLog, AgvService } from '../agv.service'
@Component({
  selector: 'app-agv-log',
  standalone: true,
  imports: [],
  templateUrl: './agv-log.component.html',
  styleUrl: './agv-log.component.css'
})
export class AgvLogComponent {
  private agvService: AgvService = inject(AgvService);
  public agvLogs$: Signal<AgvLog[]|null> = this.agvService.getAgvLogs$();
}
