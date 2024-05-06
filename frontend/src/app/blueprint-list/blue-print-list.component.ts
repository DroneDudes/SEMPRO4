import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, ViewChild } from '@angular/core';
import { BlueprintFormComponent } from '../blueprint-form/blueprint-form.component';

@Component({
  selector: 'app-blue-print-list',
  standalone: true,
  imports: [BlueprintFormComponent],
  templateUrl: './blue-print-list.component.html',
  styleUrl: './blue-print-list.component.css'
})
export class BluePrintListComponent implements OnInit {

  public blueprintResponse: any;
  public partsResponse: any
  
  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.getResponse();
  }

  public getResponse() {
    this.http.get('http://localhost:8080/api/v1/blueprints/all').subscribe({
      next: (data) => {
        this.blueprintResponse = data;
      }
    });
  }

  public getParts(blueprintId: string) {
    console.log(blueprintId);
    this.http.get(`http://localhost:8080/api/v1/blueprints/${blueprintId}`).subscribe({
      next: (response: any) => {
        this.partsResponse = response.parts;
      }
    });
  }

  public refreshBlueprints() {
    this.http.get('http://localhost:8080/api/v1/blueprints/all').subscribe({
      next: (data) => {
        this.blueprintResponse = data;
      }
    });
  }
}
