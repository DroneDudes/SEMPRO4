import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SseLogComponent } from './sse-log.component';

describe('SseLogComponent', () => {
  let component: SseLogComponent;
  let fixture: ComponentFixture<SseLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SseLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SseLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
