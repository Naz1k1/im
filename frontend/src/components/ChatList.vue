<script setup>
import { ref } from 'vue'
import { Plus, Search, UserFilled, CirclePlus } from "@element-plus/icons-vue";

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

const handleMenuClick = (type) => {
  if (type === 'friend') {
    emit('add-friend')
  } else {
    emit('create-group')
  }
}
</script>

<template>
  <div class="chat-list">
    <!-- 搜索框和添加按钮 -->
    <div class="search-box">
      <div class="search-input">
        <el-input
            v-model="searchQuery"
            placeholder="搜索"
            clearable
            :prefix-icon="Search"
        >
        </el-input>
      </div>
      <div class="add-button">
        <el-dropdown trigger="click">
          <el-button type="text">
            <el-icon><Plus /></el-icon>
          </el-button>
          
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleMenuClick('friend')">
                <div class="dropdown-item">
                  <el-icon><UserFilled /></el-icon>
                  添加好友
                </div>
              </el-dropdown-item>
              <el-dropdown-item @click="handleMenuClick('group')">
                <div class="dropdown-item">
                  <el-icon><CirclePlus /></el-icon>
                  创建群聊
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
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
  display: flex;
  align-items: center;
}

.search-input {
  flex: 1;
  margin-right: 8px;
}

.add-button {
  display: flex;
  align-items: center;
}

.add-button .el-button {
  padding: 8px;
  height: 32px;
  width: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: #07c160 !important;
  color: white !important;
}

:deep(.el-dropdown-menu__item:hover .el-icon) {
  color: white !important;
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