import {
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideFileRouter, requestContextInterceptor } from '@analogjs/router';
import { withComponentInputBinding, withNavigationErrorHandler } from '@angular/router';
import { authInterceptor } from '@/app/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideFileRouter(
		withComponentInputBinding(),
		withNavigationErrorHandler(console.error)
	),
    provideHttpClient(
      withInterceptors([requestContextInterceptor, authInterceptor])
    ),
    provideClientHydration(withEventReplay()),
  ],
};
