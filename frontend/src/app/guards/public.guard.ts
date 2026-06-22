import AuthService from "@/app/pages/(auth)/auth.service";
import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { map } from "rxjs";

export const publicGuard: CanActivateFn = (route, state) => {
	const authService = inject(AuthService);
	const router = inject(Router);

	return authService.checkIfUserIsLogged().pipe(
		map(isLoggedIn => {
			if (isLoggedIn) {
				return router.createUrlTree(['/']);
			}
			return true;
		})
	);
};