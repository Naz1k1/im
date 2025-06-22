import {api} from "./index.js";

export const WebSocketAPI = {
    // 推送消息给指定用户
    pushToUser(data) {
        return api.post('websocket/test/push-to-user', data)
    },

    // 推送广播消息
    pushToAll(data) {
        return api.post('websocket/test/push-to-all', data)
    },

    // 推送频道消息
    pushToChannel(data) {
        return api.post('websocket/test/push-to-channel', data)
    },

    // 设置用户在线状态
    setUserOnline(data) {
        return api.post('websocket/test/set-online', data)
    },

    // 设置用户离线状态
    setUserOffline(data) {
        return api.post('websocket/test/set-offline', data)
    },

    // 检查用户在线状态
    checkUserOnline(userId) {
        return api.get(`websocket/test/check-online/${userId}`)
    },

    // 推送系统通知
    pushSystemNotification(data) {
        return api.post('websocket/test/system-notification', data)
    }
} 