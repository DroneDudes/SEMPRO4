import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MachineLogComponent } from './machine-log.component';

describe('MachineLogComponent', () => {
  let component: MachineLogComponent;
  let fixture: ComponentFixture<MachineLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MachineLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MachineLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
