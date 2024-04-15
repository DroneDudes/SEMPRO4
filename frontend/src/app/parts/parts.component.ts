import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { PartsListComponent } from '../parts-list/parts-list.component';

@Component({
  selector: 'app-parts',
  standalone: true,
  imports: [ReactiveFormsModule, PartsListComponent],
  templateUrl: './parts.component.html',
  styleUrl: './parts.component.css'
})
export class PartsComponent {
  partForm: FormGroup = new FormGroup({
  name: new FormControl(''),
  description: new FormControl(''),
  specifications: new FormControl(''),
  supplierDetails: new FormControl(''),
  price: new FormControl('')
})

constructor(private http: HttpClient) { }

onSubmit() {
  if (this.partForm.valid) {
    const partData = this.partForm.value;
    this.http.post<any>('http://localhost:8080/api/v1/parts/create', partData)
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
