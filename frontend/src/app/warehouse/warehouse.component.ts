import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { WarehouseService } from './_services/warehouse.service';
import { Warehouse } from './_models/Warehouse';
import { Part } from './_models/Part';
import { WarehouseModel } from './_models/WarehouseModel';
import { SseService } from './_services/sse.service';
import { delay } from 'rxjs/operators';
import { Notification } from './_models/Notification';



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
  selectedPart: Part | null = null;
  selectedPartIndex: number | null = null;
  warehouseModels: WarehouseModel[] = [];
  notification: Notification | null = null;




  constructor(private sseService: SseService, private cd: ChangeDetectorRef) {}

  ngOnInit() {
    this.listenToWarehouseUpdates();
    this.warehouseService.getWarehouses().subscribe({
      next:(warehouses: Warehouse[]) => {
        this.warehouses= warehouses;
        
      },
      error: (error) => {
        console.error('No Warehouses', error);
      }

    }); 
  }

  async showNotification(): Promise<void> {
    document.getElementById("notinoti")?.classList.remove("hidden");
    console.log("added");
    setTimeout(() => {
      document.getElementById("notinoti")?.classList.add("hidden");
      console.log("removed");
    }, 2000);
  }


  listenToWarehouseUpdates() {
    this.sseService.getServerSentEvent('http://localhost:8080/sse/v1/warehouses').subscribe({
      next: (data: string) => {
        this.warehouses = JSON.parse(data);
        this.refreshSelectedWarehouse();
      },
      error: (error) => {
        console.error('Failed to connect to SSE', error);
      }
    });
  }

  refreshSelectedWarehouse() {
    if (this.selectedWarehouse) {
      const updatedWarehouse = this.warehouses.find(w => w.id === this.selectedWarehouse!.id);
      if (updatedWarehouse) {
        this.selectedWarehouse = updatedWarehouse;
        this.selectedWarehouseTrayId = new Array(this.selectedWarehouse.size + 1).fill(undefined);
      }
      this.cd.detectChanges();
    }
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
    this.updateParts();
    console.log(this.parts);
    const modal = document.querySelector<HTMLDialogElement>(`#my_modal_1.modal-modal-${trayIndex}`);
    modal?.showModal();
  }


  removeItemFromWarehouseWithTrayId(id: number, trayId: number) {
    this.warehouseService.removeItemFromWarehouseWithTrayId(id, trayId).subscribe({
      next: (updatedWarehouse) => {
        if (this.selectedWarehouse?.id === id) {
          this.selectedWarehouse = updatedWarehouse;
          this.createAndDisplayNotification("success", "Successfully removed part!");
        }
        console.log("Item removed successfully. UI updated.");
      },
      error: (error) => {
        console.error("Error removing item:", error);
      }
      }
    )
  }

  //Type can either be 'sucess', 'info', 'warning' or 'error'.
  createAndDisplayNotification(type: string, message: string) {
    this.notification = new Notification(message, type);
    this.showNotification();
    console.log("Showing");
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

  selectPart(index: number) {
    this.selectedPart = this.parts[index];
    this.selectedPartIndex = index;
    console.log(this.selectedPart);
  }

  addPart(id: number, trayId: number, part: Part | null) {
    if(part === null){
      console.error("Item is null");
    } else {
      this.warehouseService.addItemToWarehouseWithTrayId(id, trayId, part).subscribe(
        response => {
            this.warehouseService.getWarehouses();
            this.createAndDisplayNotification("success","Successfully added part!");
        },
        error => {
            console.error('Error:', error);
        }
    );
    }
  }


  getWarehouseModels(){
    this.warehouseService.getWarehouseModels().subscribe({
      next:(warehouseModels: WarehouseModel[]) => {
        this.warehouseModels = warehouseModels;
      },
      error: (error) => {
        console.error('Server error for WarehouseModel', error);
      }
    }); 
  }

  createWarehouse(): void {
    const warehouseCreationInformation = document.getElementById("warehouseCreationInformation");
    if (warehouseCreationInformation) {
      warehouseCreationInformation.innerText = "Not yet implemented!";
    }
  }
  removeNotYetImplemented(): void {
    const warehouseCreationInformation = document.getElementById("warehouseCreationInformation");
    if (warehouseCreationInformation) {
      warehouseCreationInformation.innerText = "";
    }
  }
}
