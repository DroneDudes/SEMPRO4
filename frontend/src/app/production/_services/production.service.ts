import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProductionBatch } from '../_models/ProductionBatch';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductionService {
  private productionUrl = "http://localhost:8080/api/v1/production";
  private orchestratorApiUrl = 'http://localhost:8080/api/v1/orchestrator';
  private blueprintsApiUrl = 'http://localhost:8080/api/v1/blueprints';

  httpClient: HttpClient = inject(HttpClient);

  getProductionBatches(): Observable<ProductionBatch[]> {
    return this.httpClient.get<ProductionBatch[]>(this.productionUrl);
  }

  getAllBlueprints(): Observable<any> {
    return this.httpClient.get<any>(`${this.blueprintsApiUrl}/all`);
  }

  startProduction(amount: number, blueprint: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const body = JSON.stringify(blueprint);
    return this.httpClient.post<any>(`${this.orchestratorApiUrl}/startProduction?amount=${amount}`, body, { headers });
  }

  stopProduction(productionId: string): Observable<any> {
    return this.httpClient.post<void>(`${this.orchestratorApiUrl}/stopProduction?productionId=${productionId}`, {});
  }
}