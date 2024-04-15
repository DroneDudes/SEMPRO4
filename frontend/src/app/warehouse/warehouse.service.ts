import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Warehouse, Item } from './warehouse.models';

@Injectable({
  providedIn: 'root'
})
export class WarehouseService {

  private baseUrl = 'http://localhost:8080/api/v1/warehouses';

  constructor(private http: HttpClient) { }

  getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(`${this.baseUrl}`);
  }

  getWarehouse(id: number): Observable<Warehouse> {
    return this.http.get<Warehouse>(`${this.baseUrl}/${id}`);
  }

  addItemToWarehouse(id: number, item: Item): Observable<Warehouse> {
    return this.http.post<Warehouse>(`${this.baseUrl}/${id}/items`, item);
  }


}
