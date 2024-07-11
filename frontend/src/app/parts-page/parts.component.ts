import { Component } from '@angular/core';
import { PartsListComponent } from '../parts-list/parts-list.component';
import { PartFormComponent } from '../part-form/part-form.component';

@Component({
  selector: 'app-parts',
  standalone: true,
  imports: [PartsListComponent, PartFormComponent],
  templateUrl: './parts.component.html',
  styleUrl: './parts.component.css'
})
export class PartsComponent {

}
