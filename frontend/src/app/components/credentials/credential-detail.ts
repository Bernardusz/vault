import { CredentialDetail } from "@/app/types/credential-type";
import { Component, input } from "@angular/core";

@Component({
	standalone: true,
	selector: "credential-detail",
	template: `
		<div class="flex flex-col gap-4 w-full justify-center items-center">
			<h2>
				{{ credentialData().serviceName }}
			</h2>
			<hr />
			<p>{{ credentialData().description }}</p>
			<div class="flex flex-row gap-2">
				<label for="username"> Username </label>
				<div class="flex flex-row gap2">
					<input
						type="text"
						[value]="credentialData().username"
						readonly
					/>
					<button
						(click)="copyToClipboard(credentialData().username)"
					>
						📋 Copy
					</button>
				</div>
			</div>
			<div class="flex flex-row gap-2">
				<label for="password"> Password </label>
				<div class="flex flex-row gap2">
					<input
						type="password"
						[value]="credentialData().password"
						readonly
					/>
					<button
						(click)="copyToClipboard(credentialData().password)"
					>
						📋 Copy
					</button>
				</div>
			</div>
		</div>
	`,
})
export default class CredentialDetailComponent {
	credentialData = input.required<CredentialDetail>();

	copyToClipboard(text: string) {
		navigator.clipboard
			.writeText(text)
			.then(() => {
				console.log("Password copied to clipboard successfully!");
			})
			.catch((err) => {
				console.error("Could not copy text: ", err);
			});
	}
}
