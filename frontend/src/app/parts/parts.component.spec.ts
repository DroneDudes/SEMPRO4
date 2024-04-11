import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartsComponent } from './parts.component';

describe('BlueprintsComponent', () => {
  let component: PartsComponent;
  let fixture: ComponentFixture<PartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
