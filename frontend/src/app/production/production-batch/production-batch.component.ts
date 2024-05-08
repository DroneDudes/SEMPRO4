import { Component, OnInit, inject } from '@angular/core';
import { ProductionBatch } from '../_models/ProductionBatch';
import { ProductionService } from '../_services/production.service';

@Component({
  selector: 'app-production-batch',
  standalone: true,
  imports: [],
  templateUrl: './production-batch.component.html',
  styleUrl: './production-batch.component.css'
})
export class ProductionBatchComponent implements OnInit {
  public tableHeaders: string[] = ["Batch number", "Start time", "Elapsed time", "Completion time", "Employee id"];
  public productionBatches: ProductionBatch[] = [
    // { batchNumber: 1, startTime: "2021-01-01 12:00:00", elapsedTime: "00:00:00", completionTime: "2021-01-01 13:00:00", employeeId: 1 }
  ];

  productionService: ProductionService = inject(ProductionService);

  ngOnInit(): void {
    this.productionService.getProductionBatches().subscribe({
      next: (productionBatches: ProductionBatch[]) => {
        this.productionBatches = productionBatches;
      },
      error: (error) => {
        console.error('No Production Batches', error);
      }
    });
  }

}
