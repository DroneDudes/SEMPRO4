import { Component } from '@angular/core';
import { AgvComponent } from '../agv/agv.component';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [AgvComponent],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {
  
}
