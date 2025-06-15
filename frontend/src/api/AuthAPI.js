import {api} from "./index.js";

export const authAPI = {
    /**
     * 登录方法
     * @param data
     */
    login(data) {
        return api.post('auth/login', data)
    },

    /**
     * 注册方法
     * @param data
     */
    register(data) {
        return api.post('auth/register', data)
    },

    /**
     * 注销方法
     */
    logout() {
        localStorage.clear();
        return api.post('auth/logout')
    },


}