<script setup>
import { ref } from 'vue'

const props = defineProps({
  activeChat: {
    type: Object,
    default: null
  },
  messages: {
    type: Array,
    default: () => []
  },
  userInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['send-message', 'view-group-info'])
const messageInput = ref('')

const handleSend = () => {
  if (!messageInput.value.trim()) return
  emit('send-message', messageInput.value)
  messageInput.value = ''
}
</script>

<template>
  <div class="chat-window">
    <template v-if="activeChat">
      <div class="chat-header">
        <span class="chat-title">{{ activeChat.name }}</span>
        <div class="chat-actions" v-if="activeChat.type === 'group'">
          <el-button type="text" @click="emit('view-group-info')">群聊信息</el-button>
        </div>
      </div>
      <div class="message-list">
        <div
          v-for="(message, index) in messages"
          :key="index"
          class="message-item"
          :class="{ 'self': message.isSelf }"
        >
          <el-avatar
            :size="30"
            :src="message.isSelf ? userInfo.avatar : activeChat.avatar"
          />
          <div class="message-content">
            <div class="message-text">{{ message.content }}</div>
            <div class="message-time">{{ message.timestamp }}</div>
          </div>
        </div>
      </div>
      <div class="message-input">
        <div class="toolbar">
          <i class="el-icon-picture-outline"></i>
          <i class="el-icon-folder"></i>
        </div>
        <el-input
          v-model="messageInput"
          type="textarea"
          :rows="3"
          placeholder="输入消息..."
          @keyup.enter.native.prevent="handleSend"
        />
        <el-button type="primary" @click="handleSend">发送</el-button>
      </div>
    </template>
    <div v-else class="no-chat-selected">
      <el-empty description="选择一个聊天开始会话" />
    </div>
  </div>
</template>

<style scoped>
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
}

.chat-header {
  height: 60px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.chat-title {
  font-size: 16px;
  font-weight: 500;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #f5f5f5;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.message-item.self {
  flex-direction: row-reverse;
}

.message-content {
  max-width: 60%;
}

.message-text {
  background-color: #fff;
  padding: 10px 15px;
  border-radius: 4px;
  word-break: break-word;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.message-item.self .message-text {
  background-color: #95ec69;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  text-align: right;
}

.message-input {
  background-color: #f5f5f5;
  border-top: 1px solid #e6e6e6;
  padding: 10px 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.toolbar {
  display: flex;
  gap: 15px;
  padding: 5px 0;
}

.toolbar i {
  font-size: 20px;
  color: #666;
  cursor: pointer;
}

.toolbar i:hover {
  color: #333;
}

.message-input .el-textarea {
  background-color: #fff;
}

.message-input .el-button {
  align-self: flex-end;
}

.no-chat-selected {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}
</style> 