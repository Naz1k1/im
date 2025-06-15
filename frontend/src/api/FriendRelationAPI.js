import {api} from "./index.js";

export const FriendRelationAPI = {
    getFriendList() {
        return api.get("friend/list");
    },
    getOnlineFriendList() {
        return api.get("friend-relation/online");
    },
    /**
     * 获取好友请求列表
     */
    getFriendRequests() {
        return api.get('friend/requests')
    },
    /**
     * 处理好友请求
     * @param {number} notificationId
     * @param {boolean} accept
     */
    handleFriendRequest(notificationId, accept) {
        return api.post(`friend/requests/${notificationId}/handle?accept=${accept}`)
    }
}