import CredentialCreateForm from "@/app/components/credentials/credential-create";
import { Component, computed, inject, OnInit, signal } from "@angular/core";
import AuthService from "@/app/pages/(auth)/auth.service";
import CredentialCreateService from "./create.service";
import { CredentialCreate } from "@/app/types/credential-type";
import { Router } from "@angular/router";
import { authGuard } from "@/app/guards/auth.guard";
import { authzGuard } from "@/app/guards/authz.guard";
import { RouteMeta } from "@analogjs/router";

export const metaRoute: RouteMeta = {
    canActivate: [authGuard, authzGuard],
};

@Component({
    standalone: true,
    imports: [CredentialCreateForm],
    template: `
        <main class="page">
            <section class="section">
                <credential-create
                    [userId]="userId()"
                    (submit)="createCredential($event)"
                    [isSubmitting]="isSubmitting()"
                />
            </section>
        </main>
    `,
})
export default class CredentialCreatePage implements OnInit {
    private readonly authService = inject(AuthService);
    private readonly credentialCreateService = inject(CredentialCreateService);
    private readonly router = inject(Router);

    userId = signal<number>(0);

    ngOnInit(): void {
        this.userId.set(this.authService.currentUser()?.id ?? 0);
    }

    protected isSubmitting = signal(false);

    createCredential(credentialData: CredentialCreate) {
        if (this.isSubmitting()) return;
        this.isSubmitting.set(true);

        this.credentialCreateService
            .createCredential(credentialData)
            .subscribe({
                next: (credentialId) => {
                    this.isSubmitting.set(false);
                    if (location)
                        this.router.navigate(["/credentials", credentialId]);
                },
                error: (err) => {
                    console.error("Error: ", err);
                },
            });
    }
}
