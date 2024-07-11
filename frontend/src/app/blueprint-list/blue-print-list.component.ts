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

  public deleteCheck:boolean = true;

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
    this.http.get(`http://localhost:8080/api/v1/blueprints/${blueprintId}`).subscribe({
      next: (response: any) => {
        this.partsResponse = response.parts;
      }
    });
  }

  public deleteBlueprint(blueprintProductTitle: string, blueprintId: string) {

    const confirmationElement = document.getElementById(blueprintProductTitle);
    const span = document.createElement("span");
    span.innerHTML = "Are you sure?";
    const yesButton = document.createElement("button");
    yesButton.innerHTML = "Yes";
    const noButton = document.createElement("button");
    noButton.innerHTML = "No";
    if (confirmationElement && this.deleteCheck) {
      confirmationElement.appendChild(span);
      confirmationElement.appendChild(yesButton);
      confirmationElement.appendChild(noButton);
    }
    this.deleteCheck = false;
    yesButton.addEventListener("click", () => {
    this.http.delete(`http://localhost:8080/api/v1/blueprints/delete/${blueprintId}`).subscribe({
      next: (response: any) => {
        this.refreshBlueprints();
        this.deleteCheck = true;
        const confirmationMessageDiv = document.getElementById("confirmationDiv1");
        const message = document.createElement("h2");
        confirmationMessageDiv?.appendChild(message);
        if(message){
        message.style.backgroundColor = "green";
        message.className = "flex-grow text-white h-10 flex justify-center rounded-lg";
        message.innerHTML = "Deleted Successfully";
        }

        setTimeout(() => {
          message.remove();
        }, 2000);
      }
    });
  });

  noButton.addEventListener("click", () => {
    span.remove();
    yesButton.remove();
    noButton.remove();
    this.deleteCheck = true;
    return;
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
