import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AppService } from './app.service';
import { AppBaseService } from './app.base.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [
    { provide: AppService, useClass: AppBaseService }
  ]
})
export class AppComponent {
  title: string = '';
  version: string = '';

  constructor(private appService: AppService) {
    this.title = this.appService.getTitle();
    this.version = this.appService.getVersion();
  }
}
