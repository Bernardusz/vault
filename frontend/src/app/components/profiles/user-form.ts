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
	selector: "user-form",
	imports: [HlmCardImports, RouterLink],
	template: `
		<hlm-card size="default" class="min-w-96 w-full">
			<hlm-card-header>
				<h2 hlmCardTitle>Sign Up</h2>
				<p hlmCardDescription>Create an account today!</p>
			</hlm-card-header>
			<div hlmCardContent>
				<form
					(submit)="handleSubmit($event)"
					class="flex flex-col gap-4"
				>
					<div class="flex flex-col gap-2">
						<label for="username">Username</label>
						<input
							[value]="userData().username"
							type="text"
							id="username"
							name="username"
							required
						/>
					</div>
					<div class="flex flex-col gap-2">
						<label for="email">Email</label>
						<input
							[value]="userData().email"
							type="email"
							id="email"
							name="email"
							required
						/>
					</div>
					<button
						class="bg-primary rounded-2xl btn-primary p-2 text-center text-background"
						[disabled]="isSubmitting()"
						type="submit"
					>
						{{ isSubmitting() ? "Creating Account..." : "Submit" }}
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
export default class UserForm {
	submit = output<UserInformationUpdate>();
	isSubmitting = input.required<boolean>();
	userData = input.required<UserPublicInformation>();

	async handleSubmit(event: Event) {
		event.preventDefault();

		const formData = new FormData(event.target as HTMLFormElement);
		const username = formData.get("username");
		const email = formData.get("email");

		if (!username || !email) {
			return;
		}

		this.submit.emit({
			username: username as string,
			email: email as string,
		});
	}
}
