import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import AuthService from "../pages/(auth)/auth.service";
import { map, take } from "rxjs";

export const authzGuard: CanActivateFn = (route, state) => {
	const authService = inject(AuthService);
	const router = inject(Router);

	const pathUserId = route.paramMap.get("userId");
	console.log(pathUserId);

	if (!pathUserId) {
		return router.createUrlTree(["/"]);
	}

	return authService.checkIfUserIsLogged().pipe(
		take(1),
		map((isLoggedIn) => {
			const user = authService.currentUser();

			if (isLoggedIn && user && String(user.id) === pathUserId) {
				return true;
			}

			return router.createUrlTree(["/"]);
		}),
	);
};
