import { Routes } from '@angular/router';
import {OverviewComponent} from "./overview/overview.component";
import {ProductionComponent} from "./production/production.component";
import {ManagementComponent} from "./management/management.component";
import {BlueprintsComponent} from "./blueprints/blueprints.component";

export const routes: Routes = [
  {path: "", component: OverviewComponent},
  {path: "production", component: ProductionComponent},
  {path: "blueprints", component: BlueprintsComponent},
  {path: "management", component: ManagementComponent},
  {path: "**", component: OverviewComponent}
];
