import {api} from "./index.js";

export const UserAPI = {
    // 获取用户信息
    getUserInfo() {
        return api.get('user/info')
    },

    // 获取聊天列表（包括好友和群聊）
    getChatList() {
        return api.get('chat/list')
    },

    // 添加好友
    addFriend(username) {
        return api.post('friend/add', { username })
    },

    // 创建群聊
    createGroup(groupInfo) {
        return api.post('group/create', groupInfo)
    },

    // 获取群聊信息
    getGroupInfo(groupId) {
        return api.get(`group/${groupId}/info`)
    },

    // 加入群聊
    joinGroup(groupId) {
        return api.post(`group/${groupId}/join`)
    },

    // 退出群聊
    leaveGroup(groupId) {
        return api.post(`group/${groupId}/leave`)
    },

    // 删除群聊（群主）
    deleteGroup(groupId) {
        return api.delete(`group/${groupId}`)
    },

    // 删除好友
    deleteFriend(userId) {
        return api.delete(`friend/${userId}`)
    }
}