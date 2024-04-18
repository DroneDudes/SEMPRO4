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
      console.log(blueprintData);
      
      this.http.post<any>('http://localhost:8080/api/v1/blueprints/create', blueprintData)
        .subscribe(
          response => {
            console.log("Inshallah!")
            console.log("Success! Response:", response); 
            const successDiv = document.getElementById('successAlert');
            const successNotification = document.createElement('span');
            successNotification.innerHTML = "Success";
  
            successDiv?.append(successNotification);
          },
          error => {
            console.log("Mahiba!")
          }
        );
    } 
  }

}
