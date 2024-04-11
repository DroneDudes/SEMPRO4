import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavbarComponent} from "./navbar/navbar.component";
import {OverviewComponent} from "./overview/overview.component";
import {ProductionComponent} from "./production/production.component";
import {ManagementComponent} from "./management/management.component";
import {PartsComponent} from "./parts/parts.component";
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, OverviewComponent, ProductionComponent, PartsComponent, ManagementComponent, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
