import { Component } from '@angular/core';

interface ProductionBatch {
  batchNumber: number;
  startTime: string;
  elapsedTime: string;
  completionTime: string;
  employeeId: number;
}

@Component({
  selector: 'app-production',
  standalone: true,
  imports: [],
  templateUrl: './production.component.html',
  styleUrl: './production.component.css'
})
export class ProductionComponent {
  public tableHeaders: string[] = ["Batch number", "Start time", "Elapsed time", "Completion time", "Employee id"];
  public productionBatches: ProductionBatch[] = [
    { batchNumber: 1, startTime: "2021-01-01 12:00:00", elapsedTime: "00:00:00", completionTime: "2021-01-01 13:00:00", employeeId: 1 }
  ];
}
