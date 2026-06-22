import { Component, input, output, signal } from "@angular/core";
import { HlmCardImports } from "@spartan-ng/helm/card";
import { LoginRequest } from "@/app/types/auth-type";
import { RouterLink } from "@angular/router";

@Component({
	standalone: true,
	selector: "login-form",
	imports: [HlmCardImports, RouterLink],
	template: `
		<hlm-card size="default" class="min-w-96 w-full">
			<hlm-card-header>
				<h2 hlmCardTitle>Log in</h2>
				<p hlmCardDescription>Log in to your account!</p>
			</hlm-card-header>
			<div hlmCardContent>
				<form
					(submit)="handleSubmit($event)"
					class="flex flex-col gap-4"
				>
					<div class="flex flex-col gap-2">
						<label for="username">Username</label>
						<input
							type="text"
							id="username"
							name="username"
							required
						/>
					</div>
					<div class="flex flex-col gap-2">
						<label for="password">Password</label>
						<input
							type="password"
							id="password"
							name="password"
							required
						/>
					</div>
					<button
						class="bg-primary rounded-2xl btn-primary p-2 text-center text-background"
						[disabled]="isSubmitting()"
						type="submit"
					>
						Submit
					</button>
				</form>
			</div>
			<hlm-card-footer>
				<p class="text-center w-full">
					Doesn't have an account yet?
					<a routerLink="/signup">Sign Up</a>
				</p>
			</hlm-card-footer>
		</hlm-card>
	`,
	styles: [],
})
export default class LoginForm {
	submit = output<LoginRequest>();
	isSubmitting = input.required<boolean>();

	async handleSubmit(event: Event) {
		event.preventDefault();

		const formData = new FormData(event.target as HTMLFormElement);

		const username = formData.get("username");
		const password = formData.get("password");

		this.submit.emit({
			username: username as string,
			password: password as string,
		});
	}
}
