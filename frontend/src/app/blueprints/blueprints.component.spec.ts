import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlueprintsComponent } from './blueprints.component';

describe('BlueprintsComponent', () => {
  let component: BlueprintsComponent;
  let fixture: ComponentFixture<BlueprintsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlueprintsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BlueprintsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
