import { Injectable, WritableSignal, signal, effect, computed, Signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Warehouse, Item } from './warehouse.models';

@Injectable({
  providedIn: 'root'
})

export class WarehouseService {

  private warehouses$: WritableSignal<Warehouse[]> = signal([]);
  private numberOfWarehouses$: Signal<number> = computed(()=> this.warehouses$().length);
  private displayDropdown$: Signal<boolean> = computed(()=> this.numberOfWarehouses$()>5);
  private baseUrl = 'http://localhost:8080/api/v1/warehouses';
  

  constructor(private http: HttpClient) {
    effect(()=>{
      this.getAllWarehouses().subscribe(data=>this.warehouses$.set(data));
    })
   }

  private getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(`${this.baseUrl}`);
  }

  private getWarehouse(id: number): Observable<Warehouse> {
    return this.http.get<Warehouse>(`${this.baseUrl}/${id}`);
  }

  private addItemToWarehouse(id: number, item: Item): Observable<Warehouse> {
    return this.http.post<Warehouse>(`${this.baseUrl}/${id}/items`, item);
  }

  getWarehouses(){
    return this.warehouses$.asReadonly();
  }
  getDisplayDropdown(){
    return this.displayDropdown$;
  }
  
}
