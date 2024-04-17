import { TestBed } from '@angular/core/testing';

import { AgvService } from './agv.service';

describe('AgvService', () => {
  let service: AgvService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgvService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
