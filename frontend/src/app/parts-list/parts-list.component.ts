import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-parts-list',
  standalone: true,
  imports: [],
  templateUrl: './parts-list.component.html',
  styleUrl: './parts-list.component.css'
})
export class PartsListComponent {

  public partResponse: any;
  public blueprintResponse: any;
  constructor(private http: HttpClient) {

  }

  ngOnInit(): void {
    this.getResponse();
  }

  public getResponse() {
    this.http.get('http://localhost:8080/api/v1/parts').subscribe({
      next: (data) => {
        this.partResponse = data;
      }
    });
  }

  public getBlueprints(partId: string) {
    console.log(partId);
    this.http.get(`http://localhost:8080/api/v1/blueprints/part/${partId}`).subscribe({
      next: (response: any) => {
        this.blueprintResponse = response;
      }
    });
  }
}
