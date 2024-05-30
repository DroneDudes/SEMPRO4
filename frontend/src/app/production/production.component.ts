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

  constructor(private productionService: ProductionService) { }

  ngOnInit(): void {
    this.fetchBlueprints();
  }

  fetchBlueprints(): void {
    this.productionService.getAllBlueprints().subscribe(
      data => {
        this.blueprints = data;
        console.log("Fetched Blueprints:", this.blueprints);
      },
      error => {
        console.error("Error fetching blueprints:", error);
      }
    );
  }

  setSelectedBlueprint(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedIndex = selectElement.selectedIndex;
    if (selectedIndex >= 0) {
      this.selectedBlueprint = this.blueprints[selectedIndex];
      console.log("Selected Blueprint:", this.selectedBlueprint);
    }
  }

  setProductionAmount(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    this.productionAmount = parseInt(inputElement.value, 10);
  }

  startProduction(): void {
    this.submitted = true;
    console.log("Selected Blueprint:", this.selectedBlueprint);
    console.log("Production Amount:", this.productionAmount);
    if (this.selectedBlueprint && this.productionAmount > 0) {
      // Ensure that the parts array contains only objects
      this.selectedBlueprint.parts = this.selectedBlueprint.parts.map((part: any) => 
        typeof part === 'number' ? { id: part } : part
      );
      this.productionService.startProduction(this.productionAmount, this.selectedBlueprint).subscribe(response => {
        console.log("Response from backend:", response);
        alert('Production started successfully.');
      }, error => {
        console.error("Error from backend:", error);
        alert('Failed to start production.');
      });
    }
  }
}