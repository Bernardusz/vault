import { CredentialSummary } from "@/app/types/credential-type";
import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
	providedIn: "root",
})
export default class IndexService {
	private http = inject(HttpClient);
	private url = "https://localhost:8443/api/credentials";

	searchCredentials(searchQuery: string): Observable<CredentialSummary[]> {
		const params = new HttpParams().set("search", searchQuery);
		return this.http.get<CredentialSummary[]>(`${this.url}/search`, {
			params,
		});
	}
}
