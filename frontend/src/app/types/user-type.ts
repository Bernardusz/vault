export interface User {
	id: number;
	username: string;
	email: string;
	password: string;
}

export interface UserCreation {
	username: string;
	email: string;
	password: string;
}

export interface UserPublicInformation {
	id: number;
	username: string;
	email: string;
}

export interface UserInformationUpdate {
	username: string;
	email: string;
}

export interface UserPasswordUpdate {
	password: string;
}
