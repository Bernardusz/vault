import {
	UserInformationUpdate,
	UserPublicInformation,
} from "@/app/types/user-type";
import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable, switchMap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class UserService {
	private http = inject(HttpClient);
	private readonly backendUrl = "https://localhost:8443/api/user";

	editUserInformation(
		userData: UserInformationUpdate,
	): Observable<UserPublicInformation> {
		return this.http
			.put<void>(`${this.backendUrl}/update/information`, userData)
			.pipe(
				switchMap(() => {
					return this.http.get<UserPublicInformation>(
						`${this.backendUrl}/detail`,
					);
				}),
			);
	}

	editUserPassword(password: string): Observable<void> {
		return this.http
			.put<void>(`${this.backendUrl}/update/password`, { password })
			.pipe(
				switchMap(() => {
					return this.http.post<void>(
						`https://localhost:8443/api/auth/logout`,
						{},
					);
				}),
			);
	}
	deleteUser(): Observable<void> {
		return this.http.delete<void>(`${this.backendUrl}/delete`);
	}
}
