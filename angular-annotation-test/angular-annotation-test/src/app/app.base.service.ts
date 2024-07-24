import { Inject, Injectable } from '@angular/core';
import { AppService } from './app.service';
import Mock from './annotations/Mock';
import { AppMockService } from './app.mock.service';

@Injectable({
  providedIn: 'root'
})
export class AppBaseService implements AppService {
  constructor() {
  }
  
  @Mock(AppMockService)
  getTitle(): string {
    return "angular-annotation-test";
  }

  // @Mock(AppMockService)
  getVersion(): string {
    return '0.0.1';
  }
}
