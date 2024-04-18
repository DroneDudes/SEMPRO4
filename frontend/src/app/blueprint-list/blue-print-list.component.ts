import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-blue-print-list',
  standalone: true,
  imports: [],
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
}
