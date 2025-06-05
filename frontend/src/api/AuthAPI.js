import {api} from "./index.js";

export const authAPI = {
    login(data) {
        return api.post('auth/login', data)
    },
    register(data) {
        return api.post('auth/register', data)
    },

    logout() {
        return api.post('auth/logout')
    }
}