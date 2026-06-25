import { authzGuard } from "@/app/guards/authz.guard";
import {
    UserInformationUpdate,
    UserPublicInformation,
} from "@/app/types/user-type";
import { injectLoad, LoadResult, RouteMeta } from "@analogjs/router";
import { Component, inject, Input, signal } from "@angular/core";
import UserDetail from "@/app/components/profiles/user-detail";
import UserForm from "@/app/components/profiles/user-form";
import UserPasswordForm from "@/app/components/profiles/user-password-form";
import { UserService } from "./[userId].service";
import { authGuard } from "@/app/guards/auth.guard";
import { load } from "./[userId].server";
import { toSignal } from "@angular/core/rxjs-interop";
import { Router } from "@angular/router";
import AuthService from "../(auth)/auth.service";

export const metaRoute: RouteMeta = {
    canActivate: [authGuard, authzGuard],
};

@Component({
    standalone: true,
    imports: [UserDetail, UserForm, UserPasswordForm],
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
                    (click)="deleteUser()"
                    class="bg-red-500 text-background btn-primary p-2 text-center rounded-2xl"
                >
                    Delete account
                </button>
            </div>

            <section class="section">
                @if (isEditing() === false) {
                    @if (data(); as user) {
                        <user-detail [userData]="user" />
                    }
                } @else if (isEditing() === "password") {
                    <user-password-form
                        [isSubmitting]="isSubmitting()"
                        (submit)="editUserPassword($event)"
                    />
                } @else if (isEditing() === "information") {
                    @if (data(); as user) {
                        <user-form
                            [isSubmitting]="isSubmitting()"
                            [userData]="user"
                            (submit)="editUserInformation($event)"
                        />
                    }
                }
            </section>
        </main>
    `,
})
export default class ProfilePage {
    @Input() userId!: number;
    inputData = toSignal(injectLoad<typeof load>(), { requireSync: true });

    data = signal<UserPublicInformation | null>(this.inputData() || null);

    private userService = inject(UserService);
    private router = inject(Router);
    private authService = inject(AuthService);

    isEditing = signal<"information" | "password" | false>(false);
    isSubmitting = signal<boolean>(false);

    editUserInformation(userData: UserInformationUpdate) {
        if (this.isSubmitting()) return;
        this.isSubmitting.set(true);

        this.userService.editUserInformation(userData).subscribe({
            next: (userData) => {
                this.data.set(userData);
                this.isSubmitting.set(false);
                this.isEditing.set(false);
            },
            error: (error) => {
                console.error("Caught error: ", error);
            },
        });
    }

    editUserPassword(password: string) {
        if (this.isSubmitting()) return;
        this.isSubmitting.set(true);

        this.userService.editUserPassword(password).subscribe({
            next: () => {
                this.isSubmitting.set(false);
                this.authService.setLoggedOut();
                this.router.navigate(["/login"]);
                this.isEditing.set(false);
            },
            error: (error) => {
                console.error("Caught error: ", error);
            },
        });
    }
    deleteUser() {
        this.userService.deleteUser().subscribe({
            next: () => {
                this.authService.setLoggedOut();
                this.router.navigate(["/"]);
            },
            error: (error) => {
                console.error("Caught error: ", error);
            },
        });
    }
}
