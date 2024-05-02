import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgvStatusComponent } from './agv-status.component';

describe('AgvStatusComponent', () => {
  let component: AgvStatusComponent;
  let fixture: ComponentFixture<AgvStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgvStatusComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AgvStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
