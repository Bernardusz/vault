import { LoginRequest, LoginResponse } from "@/app/types/auth-type";
import { UserCreation, UserPublicInformation } from "@/app/types/user-type";
import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { catchError, map, Observable, of, switchMap, tap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export default class AuthService {
	private readonly baseUrl = "https://localhost:8443/api/auth";
	private http = inject(HttpClient);

	readonly isAuthenticated = signal<boolean | null>(null);
	public readonly currentUser = signal<UserPublicInformation | null>(null);

	setLoggedOut() {
		this.isAuthenticated.set(false);
		this.currentUser.set(null);
	}

	registerUser(userCreation: UserCreation): Observable<void> {
		return this.http.post<void>(`${this.baseUrl}/register`, userCreation);
	}

	loginAndReturnJWTToken(loginRequest: LoginRequest): Observable<void> {
		this.isAuthenticated.set(null);
		this.currentUser.set(null);
		return this.http.post<void>(`${this.baseUrl}/login`, loginRequest);
	}

	checkIfUserIsLogged(): Observable<boolean> {
		if (this.isAuthenticated() !== null) {
			return of(this.isAuthenticated() as boolean);
		}
		return this.http
			.get<UserPublicInformation | null>(`${this.baseUrl}/me`)
			.pipe(
				tap((res) => {
					this.isAuthenticated.set(res === null ? false : true);
					console.log(res);
					this.currentUser.set(res);
				}),
				map((res) => {
					return res === null ? false : true;
				}),
				catchError(() => {
					this.isAuthenticated.set(false);
					this.currentUser.set(null);
					return of(false);
				}),
			);
	}

	logout(): Observable<void> {
		return this.http.post<void>(`${this.baseUrl}/logout`, {}).pipe(
			tap(() => {
				this.setLoggedOut();
			}),
		);
	}
}
