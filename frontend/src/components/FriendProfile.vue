<template>
  <div class="friend-profile">
    <div class="header">
      <el-avatar :size="72" :src="friend.avatar" />
      <div class="info">
        <div class="name">{{ friend.nickname || friend.username }}</div>
        <div class="remark" v-if="friend.remark">备注：{{ friend.remark }}</div>
      </div>
    </div>
    <div class="action-bar">
      <el-button class="remark-btn">设置备注</el-button>
      <el-button type="primary" class="chat-btn" @click="$emit('start-chat', friend)">发起聊天</el-button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  friend: Object
})
</script>

<style scoped>
.friend-profile {
  width: 100%;
  max-width: 420px;
  margin: 40px auto 0 auto;
  background: var(--color-bg-chat);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.10);
  padding: 32px 32px 24px 32px;
  color: var(--color-text-main);
  font-family: var(--font-family);
}
.header {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 18px;
}
.info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 6px;
}
.remark {
  color: var(--color-text-secondary);
  font-size: 15px;
}
.action-bar {
  display: flex;
  justify-content: center;
  gap: 18px;
  margin-top: 32px;
}
.remark-btn {
  min-width: 110px;
  border-radius: 8px;
}
.chat-btn {
  min-width: 110px;
  border-radius: 8px;
}
</style> 