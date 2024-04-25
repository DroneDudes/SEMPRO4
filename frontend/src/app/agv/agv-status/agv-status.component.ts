import { Component, Signal, inject } from '@angular/core';
import { SseService} from '../../shared/_services/sse.service'
import { AgvEvent } from '../_models/AgvEvent.entity';

@Component({
  selector: 'app-agv-status',
  standalone: true,
  imports: [],
  templateUrl: './agv-status.component.html',
  styleUrl: './agv-status.component.css'
})
export class AgvStatusComponent {
  private sseService: SseService = inject(SseService);
  public agvEvent$: Signal<AgvEvent|null> = this.sseService.getAgvEvents$();
}
