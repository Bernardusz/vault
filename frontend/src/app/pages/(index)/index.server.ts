import { CredentialSummary } from "@/app/types/credential-type";
import { PageServerLoad } from "@analogjs/router";

export const load = async ({
	params, // params/queryParams from the request
	req, // H3 Request
	res, // H3 Response handler
	fetch, // internal fetch for direct API calls,
	event, // full request event
}: PageServerLoad) => {
	const backendUrl = "https://localhost:8443/api/credentials";
	try {
		const userData = await fetch<CredentialSummary[]>(backendUrl, {
			headers: {
				cookie: req.headers.cookie || "",
			},
		});

		return userData;
	} catch (error) {
		console.error(
			"❌ Analog server loader failed to fetch user details:",
			error,
		);
		return null; // Return a safe fallback so the SSR engine doesn't throw a hard 500 error
	}
};
