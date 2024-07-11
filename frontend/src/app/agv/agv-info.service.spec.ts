import { TestBed } from '@angular/core/testing';

import { AgvInfoService } from './agv-info.service';

describe('AgvInfoService', () => {
  let service: AgvInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgvInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
