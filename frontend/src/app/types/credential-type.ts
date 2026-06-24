export interface Credential {
	id: number;
	userId: number;
	serviceName: string;
	username: string;
	password: string;
	description: string;
}

export interface CredentialCreate {
	userId: number;
	serviceName: string;
	username: string;
	password: string;
	description: string;
}

export interface CredentialSummary {
	id: number;
	serviceName: string;
	username: string;
	description: string;
}

export interface CredentialDetail {
	id: number;
	serviceName: string;
	username: string;
	password: string;
	description: string;
}

export interface CredentialUpdateInformation {
	serviceName: string;
	username: string;
	description: string;
}

export interface CredentialUpdatePassword {
	password: string;
}
