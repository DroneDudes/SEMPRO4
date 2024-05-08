import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup, Validators} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-part-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './part-form.component.html',
  styleUrl: './part-form.component.css'
})
export class PartFormComponent {
  partForm: FormGroup = new FormGroup({
    name: new FormControl('', [
      Validators.required,
    ]),
    description: new FormControl('',[
      Validators.required,
    ]),
    specifications: new FormControl('',[
      Validators.required,
    ]),
    supplierDetails: new FormControl('',[
      Validators.required,
    ]),
    price: new FormControl('',[
      Validators.required
    ])
  })

  constructor(private http: HttpClient) { }

  onSubmit() {
    console.log(this.partForm);
    if (this.partForm.valid) {
      const partData = this.partForm.value;
      console.log(partData);
      this.http.post<any>('http://localhost:8080/api/v1/parts/createPart', partData)
        .subscribe(
          response => {
            console.log("Success! Response:", response);

            const confirmationMessageDiv = document.getElementById("successAlertParts");
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
            console.log(partData)
            console.log("Mahiba!", error);
            if(error.status === 409){
            const successDiv = document.getElementById('successAlertParts');
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

  get f(){
    return this.partForm.controls;
  }
}


