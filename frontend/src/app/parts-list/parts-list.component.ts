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

  public deleteCheck:boolean = true;

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

  public deletePart(partName: string, partId: string) {

    const confirmationElement = document.getElementById(partName);
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
    this.http.delete(`http://localhost:8080/api/v1/parts/delete/${partId}`).subscribe({
      next: (response: any) => {
        this.refreshParts();
        this.deleteCheck = true;
        const confirmationMessageDiv = document.getElementById("confirmationDiv2");
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

  public refreshParts() {
    this.http.get('http://localhost:8080/api/v1/parts').subscribe({
      next: (data) => {
        this.partResponse = data;
      }
    });
  }

}
