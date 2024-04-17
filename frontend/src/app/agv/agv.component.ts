import { Component, Signal, inject } from '@angular/core';
import { AgvEvent, AgvService } from '../agv/agv.service';

@Component({
  selector: 'app-agv',
  standalone: true,
  imports: [],
  templateUrl: './agv.component.html',
  styleUrl: './agv.component.css'
})
export class AgvComponent {
  private agvService: AgvService = inject(AgvService);
  public agvEvent$: Signal<AgvEvent|null> = this.agvService.getAgvEvents$();
}
