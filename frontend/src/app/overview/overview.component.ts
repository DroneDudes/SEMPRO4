import { Component } from '@angular/core';
import { AgvComponent } from '../agv/agv.component';
import { WarehouseComponent } from '../warehouse/warehouse.component';
import { MachineLogComponent } from '../machine-log/machine-log.component';
import { AgvStatusComponent } from '../agv/agv-status/agv-status.component';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [AgvComponent, WarehouseComponent, MachineLogComponent, AgvStatusComponent],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {

}
