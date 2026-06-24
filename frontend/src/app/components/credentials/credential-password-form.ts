import { HlmCardImports } from "@/app/components/ui/card/src";
import {
	UserCreation,
	UserInformationUpdate,
	UserPublicInformation,
} from "@/app/types/user-type";
import { Component, input, output } from "@angular/core";
import { RouterLink } from "@angular/router";

@Component({
	standalone: true,
	selector: "credential-password-form",
	imports: [HlmCardImports, RouterLink],
	template: `
		<hlm-card size="default" class="min-w-96 w-full">
			<hlm-card-header>
				<h2 hlmCardTitle>Password</h2>
				<p hlmCardDescription>Update your stored password!</p>
			</hlm-card-header>
			<div hlmCardContent>
				<form
					(submit)="handleSubmit($event)"
					class="flex flex-col gap-4"
				>
					<div class="flex flex-col gap-2">
						<label for="password">Password</label>
						<input
							type="password"
							id="password"
							name="password"
							required
						/>
					</div>
					<div class="flex flex-col gap-2">
						<label for="confirmPassword">Re-enter password</label>
						<input
							type="password"
							id="confirmPassword"
							name="confirmPassword"
							required
						/>
					</div>

					<button
						class="bg-primary rounded-2xl btn-primary p-2 text-center text-background"
						[disabled]="isSubmitting()"
						type="submit"
					>
						{{ isSubmitting() ? "Updating Password..." : "Submit" }}
					</button>
				</form>
			</div>
			<hlm-card-footer>
				<p class="text-center w-full">
					Already Signed up? <a routerLink="/login">Log in</a>
				</p>
			</hlm-card-footer>
		</hlm-card>
	`,
})
export default class CredentialPasswordForm {
	submit = output<string>();
	isSubmitting = input.required<boolean>();

	async handleSubmit(event: Event) {
		event.preventDefault();

		const formData = new FormData(event.target as HTMLFormElement);
		const password = formData.get("password");
		const confirmPassword = formData.get("confirmPassword");

		if (password !== confirmPassword) {
			alert("Passwords do not match");
			return;
		}

		this.submit.emit(password as string);
	}
}
