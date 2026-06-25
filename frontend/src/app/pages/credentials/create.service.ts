import { CredentialCreate } from "@/app/types/credential-type";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { map, Observable, switchMap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export default class CredentialCreateService {
	private http = inject(HttpClient);
	private readonly backendUrl = "https://localhost:8443/api/credentials";

	createCredential(
		credentialData: CredentialCreate,
	): Observable<string | null> {
		return this.http
			.post(this.backendUrl, credentialData, {
				observe: "response",
				responseType: "text",
			})
			.pipe(
				map((response) => {
					// ⚡ Extract the "Location" header sent by Spring Boot
					const locationHeader = response.headers.get("location");
					return locationHeader !== null
						? locationHeader.substring(
								locationHeader.lastIndexOf("/") + 1,
							)
						: null;
				}),
			);
	}
}
