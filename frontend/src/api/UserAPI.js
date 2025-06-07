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

    // 搜索用户
    searchUser(keyword) {
        return api.get('friend-relation/search-user', {
            params: { username:keyword }
        })
    },

    // 添加好友
    addFriend(friendId, remark) {
        return api.post('friend-relation/add-friend', {
            friendId:friendId,
            remark:remark
        })
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
    deleteFriend(friendId) {
        return api.delete(`friend/${friendId}`)
    },

    // 获取好友列表
    getFriendList() {
        return api.get('friend/list')
    },

    // 更新好友备注
    updateFriendRemark(friendId, remark) {
        return api.put(`friend/${friendId}/remark`, { remark })
    },

    // 设置好友权限
    setFriendPermission(friendId, permissions) {
        return api.put(`friend/${friendId}/permissions`, permissions)
    },

    // 设置星标好友
    setStarFriend(friendId, isStarred) {
        return api.put(`friend/${friendId}/star`, { isStarred })
    },

    // 加入黑名单
    addToBlacklist(friendId) {
        return api.post(`friend/${friendId}/blacklist`)
    },

    // 移出黑名单
    removeFromBlacklist(friendId) {
        return api.delete(`friend/${friendId}/blacklist`)
    }
}