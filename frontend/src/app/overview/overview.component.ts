import { Component } from '@angular/core';
import { AgvService } from '../agv/agv.service';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {
  constructor(private agvService: AgvService) { }
}
