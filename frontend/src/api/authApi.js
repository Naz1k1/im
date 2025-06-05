import {api} from "./index.js";

export const authApi = {
    login(username, password) {
        return api.post('auth/login', {
            username,
            password
        })
    }
}