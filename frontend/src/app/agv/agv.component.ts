import { Component, Signal, inject } from '@angular/core';
import { AgvStatusComponent } from './agv-status/agv-status.component';

@Component({
  selector: 'app-agv',
  standalone: true,
  imports: [AgvStatusComponent],
  templateUrl: './agv.component.html',
  styleUrl: './agv.component.css'
})
export class AgvComponent {
  
}
