import { Component } from '@angular/core';
import { WarehouseComponent } from '../warehouse/warehouse.component';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [WarehouseComponent],
  templateUrl: './overview.component.html',
})
export class OverviewComponent {

}
