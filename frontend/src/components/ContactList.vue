<template>
  <div class="contact-list">
    <!-- 搜索框 -->
    <div class="search-box">
      <el-input
        v-model="searchQuery"
        placeholder="帐号"
        :prefix-icon="Search"
        class="contact-search-input"
        clearable
      />
    </div>
    <!-- 折叠列表 -->
    <div class="contact-items">
      <div class="contact-item" @click="toggle('new-friend')">
        <span class="arrow" :class="{open: openPanel==='new-friend'}">&#8250;</span>
        <span class="label">新的朋友</span>
      </div>
      <div v-if="openPanel==='new-friend'" class="sub-list">
        <div v-if="loading" class="sub-item">加载中...</div>
        <template v-else>
          <div v-if="friendRequests.length === 0" class="sub-item">暂无新朋友</div>
          <div v-for="item in friendRequests" :key="item.id" class="sub-item friend-request-item" @click="$emit('select-friend-request', item)">
            <el-avatar :size="32" :src="item.senderAvatar" style="margin-right:10px;" />
            <div class="friend-info">
              <div class="friend-row">
                <span class="friend-name">{{ item.senderName }}</span>
                <span class="friend-time">{{ formatTime(item.createdAt) }}</span>
              </div>
              <div class="friend-msg">{{ item.content }}</div>
            </div>
          </div>
        </template>
      </div>
      <div class="contact-item" @click="toggle('group')">
        <span class="arrow" :class="{open: openPanel==='group'}">&#8250;</span>
        <span class="label">群聊</span>
        <span class="count">1</span>
      </div>
      <div v-if="openPanel==='group'" class="sub-list">
        <div class="sub-item">群聊A</div>
      </div>
      <div class="contact-item" @click="toggle('contacts')">
        <span class="arrow" :class="{open: openPanel==='contacts'}">&#8250;</span>
        <span class="label">联系人</span>
        <span class="count">{{ friends.length }}</span>
      </div>
      <div v-if="openPanel==='contacts'" class="sub-list">
        <div v-if="loadingFriends" class="sub-item">加载中...</div>
        <template v-else>
          <div v-if="friends.length === 0" class="sub-item">暂无联系人</div>
          <div v-for="item in friends" :key="item.id" class="sub-item friend-request-item" @click="$emit('select-friend', item)">
            <el-avatar :size="32" :src="item.avatar" style="margin-right:10px;" />
            <div class="friend-info">
              <div class="friend-row">
                <span class="friend-name">{{ item.nickname || item.username }}</span>
              </div>
              <div class="friend-msg">{{ item.remark }}</div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { FriendRelationAPI } from '../api/FriendRelationAPI.js'

const searchQuery = ref('')
const openPanel = ref('')
const friendRequests = ref([])
const friends = ref([])
const loading = ref(false)
const loadingFriends = ref(false)

const toggle = (panel) => {
  openPanel.value = openPanel.value === panel ? '' : panel
}

// 监听 openPanel，展开新的朋友时请求数据
watch(openPanel, async (val) => {
  if (val === 'new-friend') {
    loading.value = true
    try {
      const res = await FriendRelationAPI.getFriendRequests()
      friendRequests.value = res.data?.data || []
    } catch (e) {
      friendRequests.value = []
    } finally {
      loading.value = false
    }
  } else if (val === 'contacts') {
    loadingFriends.value = true
    try {
      const res = await FriendRelationAPI.getFriendList()
      friends.value = res.data?.data || []
    } catch (e) {
      friends.value = []
    } finally {
      loadingFriends.value = false
    }
  }
})

function formatTime(str) {
  if (!str) return ''
  // 格式：06-13 17:50
  const d = new Date(str)
  const MM = String(d.getMonth() + 1).padStart(2, '0')
  const DD = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${MM}-${DD} ${hh}:${mm}`
}
</script>

<style scoped>
.contact-list {
  width: 320px;
  background: var(--color-bg-secondary);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.search-box {
  width: 100%;
  padding: 12px 0 10px 0;
  background: var(--color-bg-secondary);
  display: flex;
  justify-content: center;
}

.contact-search-input :deep(.el-input__wrapper) {
  background: var(--color-bg-main);
  border-radius: 12px;
  border: none;
  box-shadow: none;
  padding: 0 14px;
  color: var(--color-text-main);
  height: 40px;
  min-height: 40px;
}
.contact-search-input :deep(.el-input__inner) {
  background: transparent;
  color: var(--color-text-main);
  font-size: 15px;
  border: none;
  box-shadow: none;
  height: 40px;
  line-height: 40px;
  padding: 0;
}
.contact-search-input :deep(.el-input__prefix) {
  color: var(--color-text-placeholder);
}
.contact-search-input :deep(.el-input__inner::placeholder) {
  color: var(--color-text-placeholder);
  opacity: 1;
}

.contact-items {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-top: 12px;
}

.contact-item {
  display: flex;
  align-items: center;
  height: 48px;
  padding: 0 18px;
  color: var(--color-text-main);
  font-size: 17px;
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.2s;
  position: relative;
  user-select: none;
}
.contact-item:hover {
  background: #232323;
}
.arrow {
  font-size: 18px;
  color: var(--color-text-placeholder);
  margin-right: 16px;
  width: 18px;
  display: inline-block;
  transition: transform 0.2s;
}
.arrow.open {
  transform: rotate(90deg);
}
.label {
  flex: 1;
}
.count {
  color: var(--color-text-secondary);
  font-size: 16px;
  margin-left: 8px;
}
.sub-list {
  background: var(--color-bg-main);
  padding-left: 52px;
  padding-right: 12px;
  padding-bottom: 6px;
  border-radius: 0 0 8px 8px;
}
.sub-item {
  color: var(--color-text-secondary);
  font-size: 15px;
  padding: 8px 0 0 0;
}
.friend-request-item {
  display: flex;
  align-items: flex-start;
  padding: 10px 0 6px 0;
  border-bottom: 1px solid var(--color-bg-secondary);
}
.friend-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.friend-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.friend-name {
  color: var(--color-text-main);
  font-size: 15px;
  font-weight: 500;
  margin-right: 8px;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.friend-time {
  color: var(--color-text-placeholder);
  font-size: 13px;
  flex-shrink: 0;
}
.friend-msg {
  color: var(--color-text-secondary);
  font-size: 14px;
  margin-top: 2px;
  word-break: break-all;
}
</style> 