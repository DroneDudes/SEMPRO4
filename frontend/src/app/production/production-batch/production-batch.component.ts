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
  ];

  productionService: ProductionService = inject(ProductionService);

  ngOnInit(): void {
    this.productionService.getProductionBatches().subscribe({
      next: (productionBatches: ProductionBatch[]) => {
        this.productionBatches = productionBatches;
      },
      error: (error) => {
      }
    });
  }

}
