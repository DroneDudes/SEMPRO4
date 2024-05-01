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

    // getProductionBatch(batchNumber: number): Observable<ProductionBatch> {
    //     return this.httpClient.get<ProductionBatch>(this.productionUrl + "/" + batchNumber);
    // }

    // createProductionBatch(productionBatch: ProductionBatch): Observable<ProductionBatch> {
    //     return this.httpClient.post<ProductionBatch>(this.productionUrl, productionBatch);
    // }

    // updateProductionBatch(productionBatch: ProductionBatch): Observable<ProductionBatch> {
    //     return this.httpClient.put<ProductionBatch>(this.productionUrl, productionBatch);
    // }

    // deleteProductionBatch(batchNumber: number): Observable<ProductionBatch> {
    //     return this.httpClient.delete<ProductionBatch>(this.productionUrl + "/" + batchNumber);
    // }
}