import { Component, Signal, inject } from '@angular/core';
import { AgvEvent, AgvService } from '../agv.service'

@Component({
  selector: 'app-agv-status',
  standalone: true,
  imports: [],
  templateUrl: './agv-status.component.html',
  styleUrl: './agv-status.component.css'
})
export class AgvStatusComponent {
  private agvService: AgvService = inject(AgvService);
  public agvEvent$: Signal<AgvEvent|null> = this.agvService.getAgvEvents$();
}
