import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-blueprints',
  standalone: true,
  imports: [],
  templateUrl: './blueprints.component.html',
  styleUrl: './blueprints.component.css'
})
export class BlueprintsComponent implements OnInit {

  public jsonResponse: any;
  constructor(private http: HttpClient) {

  }

  ngOnInit(): void {
    this.getResponse();
  }

  public getResponse() {
    this.http.get('http://localhost:8080/api/v1/blueprints/all').subscribe({
      next: (data) => {
        this.jsonResponse = data;
      }
    });
  }
}
