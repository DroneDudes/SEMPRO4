import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BluePrintListComponent } from './blue-print-list.component';

describe('BluePrintListComponent', () => {
  let component: BluePrintListComponent;
  let fixture: ComponentFixture<BluePrintListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BluePrintListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BluePrintListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
