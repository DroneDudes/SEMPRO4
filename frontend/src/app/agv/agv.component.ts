import { Component, Signal, inject } from '@angular/core';
import { AgvEvent, AgvService } from '../agv/agv.service';
import { AgvLogComponent } from './agv-log/agv-log.component';
import { AgvStatusComponent } from './agv-status/agv-status.component';

@Component({
  selector: 'app-agv',
  standalone: true,
  imports: [AgvLogComponent, AgvStatusComponent],
  templateUrl: './agv.component.html',
  styleUrl: './agv.component.css'
})
export class AgvComponent {
  
}
