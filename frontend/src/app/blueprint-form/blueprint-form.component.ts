import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-blueprint-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './blueprint-form.component.html',
  styleUrl: './blueprint-form.component.css'
})
export class BlueprintFormComponent {

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
    productTitle: new FormControl(''),
    description: new FormControl(''),
    partsList: new FormControl(this.partsList)
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
          },
          error => {
            console.log("Mahiba!")
          }
        );
    }
  }

}
