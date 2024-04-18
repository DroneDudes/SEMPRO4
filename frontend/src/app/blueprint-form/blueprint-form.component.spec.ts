import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlueprintFormComponent } from './blueprint-form.component';

describe('BlueprintFormComponent', () => {
  let component: BlueprintFormComponent;
  let fixture: ComponentFixture<BlueprintFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlueprintFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BlueprintFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
