import { HlmCardImports } from "@/app/components/ui/card/src";
import { CredentialCreate } from "@/app/types/credential-type";
import { Component, input, output } from "@angular/core";

@Component({
	standalone: true,
	selector: "credential-create",
	imports: [HlmCardImports],
	template: `
		<hlm-card size="default" class="min-w-96 w-full">
			<hlm-card-header>
				<h2 hlmCardTitle>Create a credential</h2>
				<p hlmCardDescription>Store your account safely today!</p>
			</hlm-card-header>
			<div hlmCardContent>
				<form
					(submit)="handleSubmit($event)"
					class="flex flex-col gap-4"
				>
					<div class="flex flex-col gap-2">
						<label for="service-name">Service Name</label>
						<input
							type="service-name"
							id="service-name"
							name="service-name"
							required
						/>
					</div>
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
					<div class="flex flex-col gap-2">
						<label for="description">Description</label>
						<textarea
							type="description"
							id="description"
							name="description"
							required
						></textarea>
					</div>
					<button
						class="bg-primary rounded-2xl btn-primary p-2 text-center text-background"
						[disabled]="isSubmitting()"
						type="submit"
					>
						{{
							isSubmitting()
								? "Creating Your Credential..."
								: "Submit"
						}}
					</button>
				</form>
			</div>
			<hlm-card-footer>
				<p>May your account stay safe! 🐧</p>
			</hlm-card-footer>
		</hlm-card>
	`,
})
export default class CredentialCreateForm {
	userId = input.required<number>();
	submit = output<CredentialCreate>();
	isSubmitting = input.required<boolean>();

	async handleSubmit(event: Event) {
		event.preventDefault();

		const formData = new FormData(event.target as HTMLFormElement);
		const serviceName = formData.get("service-name");
		const username = formData.get("username");
		const password = formData.get("password");
		const description = formData.get("description");

		if (!serviceName || !password || !username || !description) {
			return;
		}

		this.submit.emit({
			userId: this.userId(),
			serviceName: serviceName as string,
			username: username as string,
			password: password as string,
			description: description as string,
		});
	}
}
