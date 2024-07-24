import { Injectable } from '@angular/core';
import { AppService } from './app.service';

@Injectable({
  providedIn: 'root'
})
export class AppMockService implements AppService {

  constructor() { }
  getTitle(): string {
    return 'angular-annotation-mock-test';
  }

  getVersion(): string {
    return '0.0.1-test';  
  }
}
