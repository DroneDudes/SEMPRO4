import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionBatchComponent } from './production-batch.component';

describe('ProductionBatchComponent', () => {
  let component: ProductionBatchComponent;
  let fixture: ComponentFixture<ProductionBatchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionBatchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionBatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
