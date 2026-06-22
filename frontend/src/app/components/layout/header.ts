import AuthService from "@/app/pages/(auth)/auth.service";
import { Component, computed, inject, OnInit, signal } from "@angular/core";
import { Router, RouterLink } from "@angular/router";

@Component({
	selector: "app-header",
	standalone: true,
	imports: [RouterLink],
	template: `
		<header class="p-4 flex flex-row justify-between">
			<h1>Vault</h1>

			<div class="flex flex-row gap-4 items-center justify-center">
				<a [routerLink]="['/users', id()]" class="text-foreground">
					{{ nickname() }}
				</a>
				<button
					(click)="logout()"
					class="btn-primary rounded-2xl bg-primary text-background text-center p-2"
				>
					Logout
				</button>
			</div>
		</header>
	`,
})
export default class Header {
	private authService = inject(AuthService);
	private router = inject(Router);

	nickname = computed(() => {
		return this.authService.currentUser()?.username;
	});

	id = computed(() => {
		return this.authService.currentUser()?.id;
	});

	logout() {
		this.authService.logout().subscribe({
			next: () => {
				this.router.navigate(["/login"]);
			},
		});
	}
}
