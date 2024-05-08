import { Component } from '@angular/core';
import { ProductionBatchComponent } from './production-batch/production-batch.component';

@Component({
  selector: 'app-production',
  standalone: true,
  imports: [ProductionBatchComponent],
  templateUrl: './production.component.html',
  styleUrl: './production.component.css'
})
export class ProductionComponent {

}
