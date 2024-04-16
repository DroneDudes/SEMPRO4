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
  getWarehouses(): Observable<Warehouse[]>{
    return this.httpClient.get<Warehouse[]>(this.warehouseUrl)
  }
  httpClient: HttpClient = inject(HttpClient);
}
