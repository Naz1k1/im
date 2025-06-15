<template>
  <div class="friend-request-detail">
    <div class="header">
      <el-avatar :size="64" :src="request.senderAvatar" />
      <div class="info">
        <div class="name">{{ request.senderName }} <el-icon style="font-size:16px;"><UserFilled /></el-icon></div>
      </div>
    </div>
    <div class="verify-msg">
      <div class="msg-label">{{ request.senderName }}：{{ request.content }}</div>
    </div>
    <div class="detail-list">

    </div>
    <div class="action-bar">
      <el-button type="primary" class="action-btn" @click="dialogVisible = true">前往验证</el-button>
    </div>
    <el-dialog v-model="dialogVisible" title="通过好友验证" width="400px" :close-on-click-modal="false" :show-close="false" class="verify-dialog">
      <div class="dialog-content">
        <div class="dialog-label">设置备注</div>
        <el-input v-model="remark" placeholder="请输入备注" maxlength="20" show-word-limit />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" :disabled="loading">取消</el-button>
          <el-button type="danger" @click="handleRefuse" :loading="loading">拒绝</el-button>
          <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import { FriendRelationAPI } from '../api/FriendRelationAPI.js'
const props = defineProps({
  request: Object
})
const emit = defineEmits(['refuse', 'accept'])
const dialogVisible = ref(false)
const remark = ref('')
const loading = ref(false)

const handleConfirm = async () => {
  loading.value = true
  try {
    await FriendRelationAPI.handleFriendRequest(props.request.id, true)
    emit('accept', props.request)
    dialogVisible.value = false
  } finally {
    loading.value = false
  }
}
const handleRefuse = async () => {
  loading.value = true
  try {
    await FriendRelationAPI.handleFriendRequest(props.request.id, false)
    emit('refuse', props.request)
    dialogVisible.value = false
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.friend-request-detail {
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
  display: flex;
  align-items: center;
  gap: 6px;
}
.verify-msg {
  background: var(--color-bg-main);
  border-radius: 8px;
  padding: 14px 18px;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.msg-label {
  color: var(--color-text-main);
  font-size: 15px;
}
.reply-btn {
  color: var(--color-primary);
  font-size: 14px;
  margin-left: 16px;
}
.detail-row {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid var(--color-border);
}
.label {
  width: 70px;
  color: var(--color-text-secondary);
  font-size: 14px;
}
.value {
  flex: 1;
  font-size: 14px;
  color: var(--color-text-main);
}
.action-bar {
  display: flex;
  justify-content: center;
  margin-top: 18px;
}
.action-btn {
  width: 140px;
  height: 38px;
  font-size: 16px;
  border-radius: 8px;
}
.verify-dialog :deep(.el-dialog__header) {
  text-align: center;
  font-size: 18px;
  color: var(--color-text-main);
  background: var(--color-bg-secondary);
  border-bottom: none;
  padding-bottom: 0;
}
.verify-dialog :deep(.el-dialog__body) {
  background: var(--color-bg-secondary);
  color: var(--color-text-main);
  padding: 24px 20px 10px 20px;
}
.dialog-content {
  margin-bottom: 10px;
}
.dialog-label {
  color: var(--color-text-secondary);
  font-size: 15px;
  margin-bottom: 8px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding: 0 10px 10px 10px;
}
.dialog-footer .el-button--danger {
  background: var(--color-danger);
  border-color: var(--color-danger);
  color: #fff;
}
.dialog-footer .el-button--danger:hover {
  filter: brightness(1.1);
}
</style> 