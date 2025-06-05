<script setup>
import { ref } from 'vue'

const props = defineProps({
  chatList: {
    type: Array,
    required: true
  },
  activeChat: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['select-chat', 'add-friend', 'create-group'])
const searchQuery = ref('')
</script>

<template>
  <div class="chat-list">
    <!-- 搜索框 -->
    <div class="search-box">
      <el-input
        v-model="searchQuery"
        placeholder="搜索"
        prefix-icon="el-icon-search"
        clearable
      />
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="text">
        <i class="el-icon-refresh"></i> 刷新
      </el-button>
    </div>

    <!-- 聊天列表 -->
    <div class="chat-items">
      <div
        v-for="chat in chatList"
        :key="chat.id"
        class="chat-item"
        :class="{ active: activeChat?.id === chat.id }"
        @click="emit('select-chat', chat)"
      >
        <el-avatar :size="40" :src="chat.avatar" />
        <div class="chat-info">
          <div class="chat-name">{{ chat.name }}</div>
          <div class="last-message">{{ chat.lastMessage }}</div>
        </div>
        <div class="chat-meta">
          <div class="time">{{ chat.lastTime }}</div>
          <el-badge v-if="chat.unread" :value="chat.unread" class="unread-badge" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-list {
  width: 250px;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;
}

.search-box {
  padding: 10px;
  border-bottom: 1px solid #e6e6e6;
}

.action-buttons {
  padding: 10px;
  display: flex;
  justify-content: space-around;
  border-bottom: 1px solid #e6e6e6;
}

.chat-items {
  flex: 1;
  overflow-y: auto;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-item:hover {
  background-color: #f5f7fa;
}

.chat-item.active {
  background-color: #e6f3ff;
}

.chat-info {
  flex: 1;
  margin-left: 10px;
  overflow: hidden;
}

.chat-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.last-message {
  color: #999;
  font-size: 13px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-meta {
  text-align: right;
}

.time {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}
</style> 