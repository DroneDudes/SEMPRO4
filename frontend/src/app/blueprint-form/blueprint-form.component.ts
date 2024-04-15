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
  blueprintForm: FormGroup = new FormGroup({
    title: new FormControl(''),
    description: new FormControl('')
  })
  
  constructor(private http: HttpClient) { }
  
  onSubmit() {
    if (this.blueprintForm.valid) {
      const blueprintData = this.blueprintForm.value;
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
