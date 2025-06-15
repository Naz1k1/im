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
        <span class="chat-title">{{ activeChat?.nickname || activeChat?.name || activeChat?.username || '未命名' }}</span>
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
  background: var(--color-bg-chat);
  color: var(--color-text-main);
  font-family: var(--font-family);
}

.chat-header {
  height: 60px;
  background: var(--color-bg-secondary);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.chat-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text-main);
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: var(--color-bg-chat);
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
  background: var(--color-bubble-other);
  color: var(--color-text-main);
  padding: 10px 15px;
  border-radius: var(--radius-bubble);
  word-break: break-word;
  box-shadow: none;
  font-size: 15px;
}

.message-item.self .message-text {
  background: var(--color-bubble-self);
  color: #fff;
}

.message-time {
  font-size: 12px;
  color: var(--color-text-placeholder);
  margin-top: 4px;
  text-align: right;
}

.message-input {
  background: var(--color-bg-secondary);
  border-top: 1px solid var(--color-border);
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
  color: var(--color-text-placeholder);
  cursor: pointer;
  transition: color 0.2s;
}

.toolbar i:hover {
  color: var(--color-text-main);
}

.message-input :deep(.el-textarea) {
  background: var(--color-bg-input);
  border-radius: var(--radius-main);
  color: var(--color-text-main);
}

.message-input :deep(.el-textarea__inner) {
  background: var(--color-bg-input);
  color: var(--color-text-main);
  border-radius: var(--radius-main);
  border: none;
}

.message-input .el-button {
  align-self: flex-end;
  background: var(--color-primary);
  color: #fff;
  border-radius: var(--radius-main);
  border: none;
}

.no-chat-selected {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-chat);
  color: var(--color-text-secondary);
}
</style> 