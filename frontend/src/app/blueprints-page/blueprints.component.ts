import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { BlueprintFormComponent } from '../blueprint-form/blueprint-form.component';
import { BluePrintListComponent } from '../blueprint-list/blue-print-list.component';

@Component({
  selector: 'app-blueprints',
  standalone: true,
  imports: [BlueprintFormComponent, BluePrintListComponent],
  templateUrl: './blueprints.component.html',
})
export class BlueprintsComponent{
}
