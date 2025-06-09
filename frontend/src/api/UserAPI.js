import {api} from "./index.js";

export const UserAPI = {
    /**
     * 获取用户信息
     */
    getUserInfo() {
        return api.get('user/info')
    },

    /**
     * 更新用户信息
     */
    updateUserInfo(data) {
        return api.put('user/info',data)
    },

    /**
     * 更新用户密码
     * @param data
     */
    updatePassword(data){
        return api.put("user/password",data)
    }


}