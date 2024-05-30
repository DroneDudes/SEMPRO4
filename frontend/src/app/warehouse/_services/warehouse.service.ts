import { Injectable, Signal, signal, computed, effect, WritableSignal, inject } from '@angular/core';
import { Warehouse } from '../_models/Warehouse';
import { Item } from '../_models/Item';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Part } from '../_models/Part';
import { WarehouseModel } from '../_models/WarehouseModel';


@Injectable({
  providedIn: 'root'
})
export class WarehouseService {
  private warehouseUrl = "http://localhost:8080/api/v1/warehouses";
  private partUrl = "http://localhost:8080/api/v1/parts";
  private modelUrl = "http://localhost:8080/api/v1/warehouses/models";
  httpClient: HttpClient = inject(HttpClient);

  getWarehouses(): Observable<Warehouse[]>{
    return this.httpClient.get<Warehouse[]>(this.warehouseUrl);
  }

  addItemToWarehouseWithTrayId(id: number, trayId: number, item: Item): Observable<Warehouse> {
    return this.httpClient.post<Warehouse>(this.warehouseUrl + "/" + id + "/items/" + trayId, item);
  }

  removeItemFromWarehouseWithTrayId(id: number, trayId: number): Observable<Warehouse> {
    return this.httpClient.delete<Warehouse>(this.warehouseUrl + "/" + id + "/items/" + trayId, {});
  }

  getParts(): Observable<Part[]> {
    return this.httpClient.get<Part[]>(this.partUrl);
  }

  getWarehouseModels(): Observable<WarehouseModel[]> {
    return this.httpClient.get<WarehouseModel[]>(this.modelUrl);
  }
}
