import {
	CredentialCreate,
	CredentialDetail,
	CredentialUpdateInformation,
} from "@/app/types/credential-type";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { map, Observable, switchMap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export default class CredentialDetailService {
	private http = inject(HttpClient);
	private readonly backendUrl = "https://localhost:8443/api/credentials";

	getCredential(id: number): Observable<CredentialDetail> {
		return this.http.get<CredentialDetail>(`${this.backendUrl}/${id}`);
	}

	updateCredentialInformation(
		id: number,
		credentialData: CredentialUpdateInformation,
	): Observable<CredentialDetail> {
		return this.http
			.put<void>(`${this.backendUrl}/${id}/information`, credentialData)
			.pipe(
				switchMap(() => {
					return this.getCredential(id);
				}),
			);
	}

	updateCredentialPassword(
		id: number,
		password: string,
	): Observable<CredentialDetail> {
		return this.http
			.put<void>(`${this.backendUrl}/${id}/password`, { password })
			.pipe(
				switchMap(() => {
					return this.getCredential(id);
				}),
			);
	}

	deleteCredential(id: number): Observable<void> {
		return this.http.delete<void>(`${this.backendUrl}/${id}`);
	}
}
