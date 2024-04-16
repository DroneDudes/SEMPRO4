import { Component, OnInit, inject } from '@angular/core';
import { WarehouseService } from './_services/warehouse.service';
import { Warehouse } from './_models/Warehouse';
import { Item } from './_models/Item';


@Component({
  selector: 'app-warehouse',
  standalone: true,
  imports: [],
  templateUrl: './warehouse.component.html',
  styleUrl: './warehouse.component.css'
})
export class WarehouseComponent implements OnInit{
  warehouseService: WarehouseService = inject(WarehouseService);
  warehouses: Warehouse[] = [];
  selectedWarehouse: Warehouse | null = null;
  selectedWarehouseTrayId: number[] = [];
  private modalIdCounter: number = 0;
  ngOnInit() {
    this.warehouseService.getWarehouses().subscribe({
      next:(warehouses: Warehouse[]) => {
        this.warehouses= warehouses;
      },
      error: (error) => {
        console.error('DER ER FANDME IKKE NOGEN WAREHOUSES MAKKER:', error);
      }

    }); 
  }
  showWarehouse(index: number): void {
    this.selectedWarehouse = this.warehouses[index];
    this.selectedWarehouseTrayId = new Array(this.selectedWarehouse.size + 1);
  }

  isItemPresent(index: number): boolean {
    return !!this.selectedWarehouse?.items[index];
  }
  
  generateModalId(): string {
    this.modalIdCounter++;
    return this.modalIdCounter.toString();
  }
}
