import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, Signal, effect } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WarehouseService } from './warehouse.service';
import { Warehouse } from './warehouse.models';

@Component({
  selector: 'app-warehouse',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouse.component.html',
  styleUrls: ['./warehouse.component.css']
})
export class WarehouseComponent {
  warehouseService: WarehouseService = inject(WarehouseService);
  warehouses: Signal<Warehouse[]> = this.warehouseService.getWarehouses();
  displayDropdown: Signal<boolean> = this.warehouseService.getDisplayDropdown();

  selectedWarehouse: Warehouse | null = null;
  
  selectedWarehouseIndex: number | null = null;
  trayIds: number[] = [];

  //ngOnInit(): void {
  //  this.loadWarehouses();
  //}

  onSelectWarehouse(index: number): void {
    this.selectedWarehouse = this.warehouses()[index];
    this.selectedWarehouseIndex = index;
    this.trayIds = Array.from({ length: this.warehouses()[index].size }, (_, i) => i + 1);
  }

  sortTrayIds(a: any, b: any): number {
    return parseInt(a.key) - parseInt(b.key);
  }
  
}
