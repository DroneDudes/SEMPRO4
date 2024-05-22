import { TestBed } from '@angular/core/testing';

import { AssemblyserviceService } from './assemblyservice.service';

describe('AssemblyserviceService', () => {
  let service: AssemblyserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssemblyserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
