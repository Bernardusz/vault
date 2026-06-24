import { CredentialSummary } from "@/app/types/credential-type";
import { Component, input } from "@angular/core";
import { RouterLink } from "@angular/router";

@Component({
	selector: "credential-card",
	standalone: true,
	imports: [RouterLink],
	template: `
		<div
			class="flex h-fit max-h-48 flex-col border-2 gap-2 p-8 rounded-2xl border-primary items-center justify-center"
		>
			<a [routerLink]="['/credentials', credential().id]">
				<div
					class="flex gap-2 flex-row items-center text-center justify-between"
				>
					<h2>
						{{ credential().serviceName }}
					</h2>
					<h3 class="text-center items-center">-</h3>
					<h2>
						{{ credential().username }}
					</h2>
				</div>
			</a>

			<p>{{ credential().description }}</p>
		</div>
	`,
})
export default class CredentialCard {
	credential = input.required<CredentialSummary>();
}
