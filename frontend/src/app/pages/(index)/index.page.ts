import { authGuard } from "@/app/guards/auth.guard";
import { RouteMeta } from "@analogjs/router";
import { Component, inject, OnInit } from "@angular/core";
import { OnSameUrlNavigation } from "@angular/router";
import AuthService from "../(auth)/auth.service";

export const routeMeta: RouteMeta = {
	canActivate: [authGuard],
};

@Component({
	selector: "app-index-page",
	templateUrl: "./index.page.html",
})
export default class IndexPage implements OnInit {
	private authService = inject(AuthService);

	ngOnInit(): void {
		this.authService.checkIfUserIsLogged().subscribe();
	}
}
