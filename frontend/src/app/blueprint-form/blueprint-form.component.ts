import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-blueprint-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './blueprint-form.component.html',
  styleUrl: './blueprint-form.component.css'
})
export class BlueprintFormComponent {
[x: string]: any;

partsList:number[] = [];


addNewPart(partId:number){
  this.partsList.push(partId);
  this.updatePartsFormControl();
}

removePart(partId:number){
  const index = this.partsList.findIndex(part => part === partId);
  if(index !== -1){
    this.partsList.splice(index, 1);
    this.updatePartsFormControl();
  }
}

updatePartsFormControl() {
  this.blueprintForm.controls['parts'].setValue(this.partsList);
}

get f(){
  return this.blueprintForm.controls;
}

public jsonResponse: any;

  ngOnInit(): void {
    this.getResponse();
  }

  public getResponse() {
    this.http.get('http://localhost:8080/api/v1/parts').subscribe({
      next: (data) => {
        this.jsonResponse = data;
      }
    });
  }

//Creating the form for blueprints

  blueprintForm: FormGroup = new FormGroup({
    productTitle: new FormControl('', [
      Validators.required,
      // Validators.minLength(4),
      // Validators.maxLength(20),
    ]),
    description: new FormControl('', [
      Validators.required,
      // Validators.minLength(50),
      // Validators.maxLength(200),
    ]),
    partsList: new FormControl(this.partsList, [
      // Validators.required,
    ])
  })

  constructor(private http: HttpClient) { }


  onSubmit() {
    if (this.blueprintForm.valid) {
      const blueprintData = this.blueprintForm.value;

      this.http.post<any>('http://localhost:8080/api/v1/blueprints/create', blueprintData)
        .subscribe(
          response => {
            const confirmationMessageDiv = document.getElementById("successAlertBlueprints");
            const message = document.createElement("h2");
            confirmationMessageDiv?.appendChild(message);
            if(message){
            message.style.backgroundColor = "green";
            message.className = "flex-grow text-white h-10 flex justify-center rounded-lg";
            message.innerHTML = "Success";
            }
            setTimeout(() => {
              location.reload();
            }, 1000);
          },
          error => {
            if(error.status === 409){
              const successDiv = document.getElementById('successAlertBlueprints');
              const successNotification = document.createElement('span');
              successNotification.innerHTML = error.error;
              successNotification.style.color = "red";
              successDiv?.append(successNotification);
              setTimeout(() => {
                successNotification.remove();
              }, 2500);
              }
          }
        );
    }
  }

}
