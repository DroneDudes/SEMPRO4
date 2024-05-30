import { Component, OnInit } from '@angular/core';
import { ProductionBatchComponent } from './production-batch/production-batch.component';
import { ProductionService } from './_services/production.service';

@Component({
  selector: 'app-production',
  standalone: true,
  imports: [ProductionBatchComponent],
  templateUrl: './production.component.html',
  styleUrls: ['./production.component.css']
})
export class ProductionComponent implements OnInit {
  blueprints: any[] = [];
  selectedBlueprint: any = null;
  productionAmount: number = 1;
  submitted: boolean = false;
  productionBegun: boolean = false;

  constructor(private productionService: ProductionService) { }

  ngOnInit(): void {
    this.fetchBlueprints();
  }

  fetchBlueprints(): void {
    this.productionService.getAllBlueprints().subscribe(
      data => {
        this.blueprints = data;
      },
      error => {
      }
    );
  }

  setSelectedBlueprint(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedIndex = selectElement.selectedIndex;
    if (selectedIndex >= 0) {
      this.selectedBlueprint = this.blueprints[selectedIndex];
    }
  }

  setProductionAmount(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    this.productionAmount = parseInt(inputElement.value, 10);
  }

  startProduction(): void {
    this.submitted = true;
    if (this.selectedBlueprint && this.productionAmount > 0) {
      // Ensure that the parts array contains only objects
      this.selectedBlueprint.parts = this.selectedBlueprint.parts.map((part: any) => 
        typeof part === 'number' ? { id: part } : part
      );
      this.productionBegun = true;
      this.productionService.startProduction(this.productionAmount, this.selectedBlueprint).subscribe(response => {
      }, error => {
      });
    
    }
  }
}