import { Injectable, Signal, signal, computed, effect, WritableSignal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductionBatch } from '../_models/ProductionBatch';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
  })

export class ProductionService {
    private productionUrl = "http://localhost:8080/api/v1/production";
    httpClient: HttpClient = inject(HttpClient);

    getProductionBatches(): Observable<ProductionBatch[]>{
        return this.httpClient.get<ProductionBatch[]>(this.productionUrl);
    }

}