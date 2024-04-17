import { Component, OnInit, inject } from '@angular/core';
import { WarehouseService } from './_services/warehouse.service';
import { Warehouse } from './_models/Warehouse';
import { Part } from './_models/Part';


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
  parts: Part[] = [];
  private modalIdCounter: number = 0;
  ngOnInit() {
    this.warehouseService.getWarehouses().subscribe({
      next:(warehouses: Warehouse[]) => {
        this.warehouses= warehouses;
      },
      error: (error) => {
        console.error('No Warehouses', error);
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
  
  showModal(trayIndex: number): void {
    const modal = document.querySelector<HTMLDialogElement>(`#my_modal_1.modal-modal-${trayIndex}`);
    modal?.showModal();
  }

  showModalAndGetParts(trayIndex: number): void {
    const modal = document.querySelector<HTMLDialogElement>(`#my_modal_1.modal-modal-${trayIndex}`);
    modal?.showModal();
    this.updateParts();
    console.log(this.parts);
  }


  removeItemFromWarehouseWithTrayId(id: number, trayId: number) {
    this.warehouseService.removeItemFromWarehouseWithTrayId(id, trayId).subscribe({
      next: (updatedWarehouse) => {
        if (this.selectedWarehouse?.id === id) {
          this.selectedWarehouse = updatedWarehouse;
        }
        console.log("Item removed successfully. UI updated.");
      },
      error: (error) => {
        console.error("Error removing item:", error);
      }
      }
    )
  }

  updateParts() {
    this.warehouseService.getParts().subscribe({
      next:(parts: Part[]) => {
        this.parts = parts;
      },
      error: (error) => {
        console.error('Server error', error);
      }

    }); 
  }
}
