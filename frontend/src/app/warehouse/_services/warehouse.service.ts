import { Injectable, Signal, signal, computed, effect, WritableSignal, inject } from '@angular/core';
import { Warehouse } from '../_models/Warehouse';
import { Item } from '../_models/Item';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class WarehouseService {
  private warehouseUrl = "http://localhost:8080/api/v1/warehouses";
  
  httpClient: HttpClient = inject(HttpClient);

  getWarehouses(): Observable<Warehouse[]>{
    return this.httpClient.get<Warehouse[]>(this.warehouseUrl)
  }

  addItemToWarehouseWithTrayId(id: number, trayId: number, item: Item): Observable<Warehouse> {
    return this.httpClient.post<Warehouse>(this.warehouseUrl + "/" + id + "/items/" + trayId, item)
  }

  removeItemFromWarehouseWithTrayId(id: number, trayId: number): Observable<Warehouse> {
    console.log("Service delete");
    return this.httpClient.delete<Warehouse>(this.warehouseUrl + "/" + id + "/items/" + trayId, {})
  }
}
