import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import Header from '@/app/components/layout/header';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header],
  template: `
  	<app-header></app-header>
  	<router-outlet></router-outlet>
  `,
})
export class App {}
