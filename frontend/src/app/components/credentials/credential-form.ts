import { HlmCardImports } from "@/app/components/ui/card/src";
import {
	CredentialSummary,
	CredentialUpdateInformation,
} from "@/app/types/credential-type";
import {
	UserCreation,
	UserInformationUpdate,
	UserPublicInformation,
} from "@/app/types/user-type";
import { Component, input, output } from "@angular/core";
import { RouterLink } from "@angular/router";

@Component({
	standalone: true,
	selector: "credential-form",
	imports: [HlmCardImports],
	template: `
		<hlm-card size="default" class="min-w-96 w-full">
			<hlm-card-header>
				<h2 hlmCardTitle>Edit the information</h2>
				<p hlmCardDescription>
					Edit public information of your stored credential!
				</p>
			</hlm-card-header>
			<div hlmCardContent>
				<form
					(submit)="handleSubmit($event)"
					class="flex flex-col gap-4"
				>
					<div class="flex flex-col gap-2">
						<label for="service-name">Service Name</label>
						<input
							[value]="credentialData().serviceName"
							type="service-name"
							id="service-name"
							name="service-name"
							required
						/>
					</div>
					<div class="flex flex-col gap-2">
						<label for="username">Username</label>
						<input
							[value]="credentialData().username"
							type="text"
							id="username"
							name="username"
							required
						/>
					</div>
					<div class="flex flex-col gap-2">
						<label for="description">Description</label>
						<textarea
							[value]="credentialData().description"
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
						{{ isSubmitting() ? "Creating Account..." : "Submit" }}
					</button>
				</form>
			</div>
			<hlm-card-footer>
				<p>May your account stay safe! 🐧</p>
			</hlm-card-footer>
		</hlm-card>
	`,
})
export default class CredentialForm {
	submit = output<CredentialUpdateInformation>();
	isSubmitting = input.required<boolean>();
	credentialData = input.required<CredentialSummary>();

	async handleSubmit(event: Event) {
		event.preventDefault();

		const formData = new FormData(event.target as HTMLFormElement);
		const serviceName = formData.get("service-name");
		const username = formData.get("username");
		const description = formData.get("description");

		if (!serviceName || !username || !description) {
			return;
		}

		this.submit.emit({
			serviceName: serviceName as string,
			username: username as string,
			description: description as string,
		});
	}
}
