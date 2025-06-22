import {api} from "./index.js";

export const UserAPI = {
    /**
     * 获取用户信息
     */
    getUserInfo() {
        return api.get('user/info')
    },

    /**
     * 上传头像
     */
    uploadAvatar(file) {
        const formData = new FormData()
        formData.append('file', file)
        return api.post('user/avatar/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    },

    /**
     * 修改密码
     */
    changePassword(data) {
        return api.post('user/password', data)
    },

    /**
     * 更新用户信息
     */
    updateUserInfo(data) {
        return api.post('user/info', data)
    },

    /**
     * 搜索用户
     */
    searchUser(keyword) {
        return api.get('user/search', {
            params: { keyword }
        })
    }
}