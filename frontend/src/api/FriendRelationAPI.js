import {api} from "./index.js";

export const FriendRelationAPI = {
    // 获取好友列表
    getFriendList() {
        return api.get("friend/list");
    },

    // 发送好友请求
    sendFriendRequest(data) {
        return api.post('friend/requests', data)
    },

    // 获取好友请求列表
    getFriendRequests() {
        return api.get('friend/requests')
    },

    // 处理好友请求
    handleFriendRequest(notificationId, accept) {
        return api.post(`friend/requests/${notificationId}/handle?accept=${accept}`)
    }
}