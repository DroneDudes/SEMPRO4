import { Component, inject, Signal } from '@angular/core';
import { AssemblyserviceService } from './assemblyservice.service';
import { AssemblyStation } from './assembly';

@Component({
  selector: 'app-assembly',
  standalone: true,
  imports: [],
  templateUrl: './assembly.component.html',
  styleUrl: './assembly.component.css'
})
export class AssemblyComponent {
private assemblyserviceService = inject(AssemblyserviceService);
public assemblyInfo$:Signal<AssemblyStation|undefined> = this.assemblyserviceService.getAssemblyInfo$();
}
