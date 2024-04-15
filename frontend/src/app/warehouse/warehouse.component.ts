import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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
export class WarehouseComponent implements OnInit {
  warehouses: Warehouse[] = [];
  selectedWarehouse: Warehouse | null = null;
  displayDropdown: boolean = false;
  selectedWarehouseIndex: number | null = null;
  trayIds: number[] = [];

  constructor(private warehouseService: WarehouseService) { }

  ngOnInit(): void {
    this.loadWarehouses();
  }

  onSelectWarehouse(index: number): void {
    this.selectedWarehouse = this.warehouses[index];
    this.selectedWarehouseIndex = index;
    this.trayIds = Array.from({ length: this.warehouses[index].size }, (_, i) => i + 1);
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data) => {
        this.warehouses = data;
        this.displayDropdown = this.warehouses.length > 5;
      },
      error: (err) => {
        console.error('Failed to get warehouses', err);
      }
    });
  }
  sortTrayIds(a: any, b: any): number {
    return parseInt(a.key) - parseInt(b.key);
  }
}
