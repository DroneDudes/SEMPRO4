import { Component, Signal, inject } from '@angular/core';
import { SseService } from '../../shared/_services/sse.service';
import { SseLog } from '../../shared/_models/sse-log';

@Component({
  selector: 'app-sse-log',
  standalone: true,
  imports: [],
  templateUrl: './sse-log.component.html',
  styleUrl: './sse-log.component.css'
})
export class SseLogComponent {
  private sseService: SseService = inject(SseService);
  public machineLogs$: Signal<SseLog[]> = this.sseService.getLogs$();
}
