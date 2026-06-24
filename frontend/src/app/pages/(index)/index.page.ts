import { authGuard } from "@/app/guards/auth.guard";
import { injectLoad, RouteMeta } from "@analogjs/router";
import {
	Component,
	computed,
	inject,
	Input,
	OnInit,
	signal,
} from "@angular/core";
import { OnSameUrlNavigation, RouterLink } from "@angular/router";
import AuthService from "../(auth)/auth.service";
import { toSignal } from "@angular/core/rxjs-interop";
import { load } from "./index.server";
import { CredentialSummary } from "@/app/types/credential-type";
import CredentialCard from "@/app/components/credentials/credential-card";
import { debounceTime, distinctUntilChanged, Subject } from "rxjs";
import IndexService from "./index.service";

export const routeMeta: RouteMeta = {
	canActivate: [authGuard],
};

@Component({
	selector: "app-index-page",
	templateUrl: "./index.page.html",
	imports: [CredentialCard, RouterLink],
	standalone: true,
})
export default class IndexPage {
	inputData = toSignal(injectLoad<typeof load>(), { requireSync: true });
	data = signal<CredentialSummary[] | null>(this.inputData() || null);

	isEditing = signal<"information" | "password" | false>(false);
	isSubmitting = signal<boolean>(false);

	private searchSubject = new Subject<string>();
	private indexService = inject(IndexService);

	constructor() {
		this.searchSubject
			.pipe(debounceTime(1000), distinctUntilChanged())
			.subscribe((searchTerm) => {
				if (!searchTerm.trim()) {
					this.data.set(this.inputData() || []);
					return;
				}

				this.indexService.searchCredentials(searchTerm).subscribe({
					next: (data) => {
						this.data.set(data);
					},
					error: (error) => {
						console.error("Error ", error);
					},
				});
			});
	}

	onSearchChange(event: Event) {
		const element = event.target as HTMLInputElement;
		this.searchSubject.next(element.value);
	}
}
