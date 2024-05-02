import { Component, Signal, inject } from '@angular/core';
import { AgvInfoService } from '../agv-info.service';
import { AgvLog } from '../_models/agv-log';

@Component({
  selector: 'app-agv-status',
  standalone: true,
  imports: [],
  templateUrl: './agv-status.component.html',
  styleUrl: './agv-status.component.css'
})
export class AgvStatusComponent {
  private agvInfoService = inject(AgvInfoService);
  public agvInfo$: Signal<AgvLog|undefined> = this.agvInfoService.getAgvInfo$();
}
