import SignupForm from "@/app/components/auth/signup-form";
import { publicGuard } from "@/app/guards/public.guard";
import AuthService from "@/app/pages/(auth)/auth.service";
import { UserCreation } from "@/app/types/user-type";
import { RouteMeta } from "@analogjs/router";
import { Component, inject, Inject, signal } from "@angular/core";
import { Router } from "@angular/router";

export const routeMeta: RouteMeta = {
	canActivate: [publicGuard],
};
@Component({
	standalone: true,
	selector: "app-login-page",
	imports: [SignupForm],
	template: `
		<main class="page">
			<section class="section flex items-center justify-center">
				<signup-form
					[isSubmitting]="isSubmitting()"
					(submit)="registerAndRedirect($event)"
				/>
			</section>
		</main>
	`,
})
export default class SignupPage {
	private authService = inject(AuthService);
	private router = inject(Router);

	protected isSubmitting = signal<boolean>(false);

	registerAndRedirect(userCreation: UserCreation) {
		if (this.isSubmitting()) return;
		this.isSubmitting.set(true);
		this.authService.registerUser(userCreation).subscribe({
			next: () => {
				this.router.navigate(["/login"]);
			},
			error: (err) => {
				console.error("Authentication breach prevented:", err);
			},
		});
	}
}
