import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
<<<<<<< HEAD
import { HttpClient } from '@angular/common/http';
import {NavbarComponent} from "./navbar/navbar.component";
import {OverviewComponent} from "./overview/overview.component";
import {ProductionComponent} from "./production/production.component";
import {ManagementComponent} from "./management/management.component";
import {PartsComponent} from "./parts-page/parts.component";
import { ReactiveFormsModule } from '@angular/forms';
import { BlueprintsComponent } from './blueprints-page/blueprints.component';
=======
>>>>>>> V1_T20_AgvDataRepresentation

@Component({
  selector: 'app-root',
  standalone: true,
<<<<<<< HEAD
  imports: [RouterOutlet, NavbarComponent, OverviewComponent, ProductionComponent, BlueprintsComponent, PartsComponent, ManagementComponent, ReactiveFormsModule],
=======
  imports: [RouterOutlet],
>>>>>>> V1_T20_AgvDataRepresentation
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
