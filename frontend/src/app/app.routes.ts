import { Routes } from '@angular/router';

import {OverviewComponent} from "./overview/overview.component";
import {PartsComponent} from "./parts-page/parts.component";
import { BlueprintsComponent } from './blueprints-page/blueprints.component';
import { ManagementComponent } from './management/management.component';
import { ProductionComponent } from './production/production.component';

export const routes: Routes = [
  {path: "production", component: ProductionComponent},
  {path: "parts", component: PartsComponent},
  {path: "management", component: ManagementComponent},
  {path: "blueprints", component: BlueprintsComponent},
  {path: "**", component: OverviewComponent}
];

