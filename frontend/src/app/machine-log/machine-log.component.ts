import { Component, Signal, inject } from '@angular/core';
import { SseLog } from '../shared/_models/sse-log';
import { SseService } from '../shared/_services/sse.service';
@Component({
  selector: 'app-machine-log',
  standalone: true,
  imports: [],
  templateUrl: './machine-log.component.html',
  styleUrl: './machine-log.component.css'
})
export class MachineLogComponent {
  private sseService: SseService = inject(SseService);
  public machineLogs$: Signal<SseLog[]> = this.sseService.getLogs$();
}