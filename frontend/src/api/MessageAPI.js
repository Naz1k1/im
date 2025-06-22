import {api} from "./index.js";

export const MessageAPI = {
    // 发送测试消息
    sendTestMessage(data) {
        return api.post('message/test/send', data)
    },

    // 异步发送测试消息
    sendAsyncTestMessage(data) {
        return api.post('message/test/send-async', data)
    },

    // 健康检查
    healthCheck() {
        return api.get('message/test/health')
    }
} 