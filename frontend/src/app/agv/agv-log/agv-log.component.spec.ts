import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgvLogComponent } from './agv-log.component';

describe('AgvLogComponent', () => {
  let component: AgvLogComponent;
  let fixture: ComponentFixture<AgvLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgvLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AgvLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
