import { Component, inject, signal } from "@angular/core";
import LoginForm from "@/app/components/auth/login-form";
import AuthService from "@/app/pages/(auth)/auth.service";
import { LoginRequest } from "@/app/types/auth-type";
import { Router } from "@angular/router";
import { publicGuard } from "@/app/guards/public.guard";
import { RouteMeta } from "@analogjs/router";

export const routeMeta: RouteMeta = {
	canActivate: [publicGuard],
};

@Component({
	standalone: true,
	selector: "app-login-page",
	imports: [LoginForm],
	template: `
		<main class="page">
			<section class="section flex items-center justify-center">
				<login-form
					[isSubmitting]="isSubmitting()"
					(submit)="loginAndStoreJWTToken($event)"
				/>
			</section>
		</main>
	`,
})
export default class LoginPage {
	private authService = inject(AuthService);
	private router = inject(Router);

	protected isSubmitting = signal<boolean>(false);

	loginAndStoreJWTToken(loginRequest: LoginRequest) {
		if (this.isSubmitting()) return;
		this.isSubmitting.set(true);
		this.authService.loginAndReturnJWTToken(loginRequest).subscribe({
			next: () => {
				this.authService.checkIfUserIsLogged().subscribe({
					next: (isLoggedIn) => {
						console.log(
							"Step 2: Verification result received:",
							isLoggedIn,
						);

						if (isLoggedIn) {
							console.log(
								"Step 3: Access granted, routing to dashboard view!",
							);
							this.router.navigate(["/"]).then(() => {
								this.isSubmitting.set(false); // Release button lock on arrival
							});
						} else {
							console.warn(
								"Step 3: Session verification failed (Cookie latency error)",
							);
							this.isSubmitting.set(false);
						}
					},
					error: (verifyErr) => {
						this.isSubmitting.set(false);
						console.error(
							"Profile state tracking crashed:",
							verifyErr,
						);
					},
				});
			},
			error: (err) => {
				console.error("Authentication breach prevented:", err);
			},
		});
	}
}
