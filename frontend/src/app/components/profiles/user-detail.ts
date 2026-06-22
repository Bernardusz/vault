import { UserPublicInformation } from "@/app/types/user-type";
import { Component, input } from "@angular/core";

@Component({
	standalone: true,
	selector: "user-detail",
	template: `
		<div class="flex flex-col gap-4 w-full justify-center items-center">
			<h2>
				{{ userData().username }}
			</h2>
			<hr />
			<p>{{ userData().email }}</p>
		</div>
	`,
})
export default class UserDetail {
	userData = input.required<UserPublicInformation>();
}
