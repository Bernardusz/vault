import { authzGuard } from "@/app/guards/authz.guard";
import { RouteMeta } from "@analogjs/router";
import {
	Component,
	inject,
	Input,
	OnDestroy,
	OnInit,
	signal,
} from "@angular/core";
import { authGuard } from "@/app/guards/auth.guard";
import CredentialDetailComponent from "@/app/components/credentials/credential-detail";
import CredentialForm from "@/app/components/credentials/credential-form";
import CredentialPasswordForm from "@/app/components/credentials/credential-password-form";
import {
	CredentialDetail,
	CredentialUpdateInformation,
} from "@/app/types/credential-type";
import CredentialDetailService from "./[credentialId].service";
import { NavigationEnd, Router } from "@angular/router";
import AuthService from "../../(auth)/auth.service";
import { filter, Subscription } from "rxjs";

export const metaRoute: RouteMeta = {
	canActivate: [authGuard, authzGuard],
};

@Component({
	standalone: true,
	imports: [
		CredentialDetailComponent,
		CredentialForm,
		CredentialPasswordForm,
	],
	template: `
		<main class="page">
			<button
				(click)="isEditing.set(false)"
				class="bg-primary z-50 absolute top-20 left-2 text-background btn-primary p-2 text-center rounded-2xl"
			>
				Go back
			</button>
			<div class="flex z-50 flex-col gap-2 absolute top-20 right-2">
				<button
					(click)="isEditing.set('information')"
					class="bg-primary text-background btn-primary p-2 text-center rounded-2xl"
				>
					Edit Information
				</button>
				<button
					(click)="isEditing.set('password')"
					class="bg-primary text-background btn-primary p-2 text-center rounded-2xl"
				>
					Edit Passowrd
				</button>
				<button
					(click)="deleteCredential(credentialId)"
					class="bg-red-500 text-background btn-primary p-2 text-center rounded-2xl"
				>
					Delete account
				</button>
			</div>

			<section class="section">
				@if (isEditing() === false) {
					@if (data(); as credential) {
						<credential-detail [credentialData]="credential" />
					}
				} @else if (isEditing() === "password") {
					<credential-password-form
						[isSubmitting]="isSubmitting()"
						(submit)="editCredentialPassword($event)"
					/>
				} @else if (isEditing() === "information") {
					@if (data(); as credential) {
						<credential-form
							[isSubmitting]="isSubmitting()"
							[credentialData]="credential"
							(submit)="editCredentialInformation($event)"
						/>
					}
				}
			</section>
		</main>
	`,
})
export default class CredentialDetailPage implements OnInit, OnDestroy {
	@Input() credentialId!: number;

	private readonly credentialDetailService = inject(CredentialDetailService);
	private router = inject(Router);
	private routerSubscription!: Subscription;

	data = signal<CredentialDetail | null>(null);
	isLoading = signal<boolean>(true);
	isEditing = signal<"information" | "password" | false>(false);
	isSubmitting = signal<boolean>(false);

	ngOnInit(): void {
		this.getData();
		this.routerSubscription = this.router.events
			.pipe(filter((event) => event instanceof NavigationEnd))
			.subscribe(() => {
				if (this.isSubmitting()) return;
				this.getData();
			});
	}

	ngOnDestroy(): void {
		if (this.routerSubscription) {
			this.routerSubscription.unsubscribe();
		}
		this.isSubmitting.set(false);
	}

	getData() {
		this.credentialDetailService
			.getCredential(this.credentialId)
			.subscribe({
				next: (data) => {
					console.log(data);
					this.data.set(data);
					this.isLoading.set(false);
				},
				error: (error) => {
					console.error("Error: ", error);
					this.isLoading.set(false);
				},
			});
	}

	editCredentialInformation(credentialData: CredentialUpdateInformation) {
		if (this.isSubmitting()) return;
		this.isSubmitting.set(true);

		this.credentialDetailService
			.updateCredentialInformation(this.credentialId, credentialData)
			.subscribe({
				next: (credentialData) => {
					this.data.set(credentialData);
					this.isSubmitting.set(false);
					this.isEditing.set(false);
				},
				error: (error) => {
					console.error("Caught error: ", error);
				},
			});
	}

	editCredentialPassword(password: string) {
		if (this.isSubmitting()) return;
		this.isSubmitting.set(true);

		this.credentialDetailService
			.updateCredentialPassword(this.credentialId, password)
			.subscribe({
				next: (credentialData) => {
					this.data.set(credentialData);
					this.isSubmitting.set(false);
					this.isEditing.set(false);
				},
				error: (error) => {
					console.error("Caught error: ", error);
				},
			});
	}
	deleteCredential(id: number) {
		if (this.isSubmitting()) return;
		this.isSubmitting.set(true);

		this.credentialDetailService.deleteCredential(id).subscribe({
			next: () => {
				this.router.navigate(["/"]);
			},
			error: (error: Error) => {
				console.error("Caught error: ", error);
			},
		});
	}
}
