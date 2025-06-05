<script setup>
import { ref, onMounted } from 'vue'
import { authAPI } from '../api/AuthAPI.js'
import { UserAPI } from "../api/UserAPI.js"
import { ElMessage } from 'element-plus'
import router from '../router'
import NavBar from '../components/NavBar.vue'
import ChatList from '../components/ChatList.vue'
import ChatWindow from '../components/ChatWindow.vue'

const userInfo = ref({
  username: '',
  nickname: ''
})

const activeChat = ref(null)
const chatList = ref([])
const messages = ref([])

// 控制弹窗显示
const addFriendDialogVisible = ref(false)
const createGroupDialogVisible = ref(false)

// 弹窗位置
const dialogPosition = ref({ x: 0, y: 0 })

// 表单数据
const friendUsername = ref('')
const groupForm = ref({
  name: '',
  description: ''
})

// 计算初始弹窗位置（屏幕中心）
const initDialogPosition = () => {
  const screenWidth = window.innerWidth
  const screenHeight = window.innerHeight
  const dialogWidth = 400 // 弹窗宽度
  const dialogHeight = 250 // 估算的弹窗高度
  
  dialogPosition.value = {
    x: (screenWidth - dialogWidth) / 2,
    y: (screenHeight - dialogHeight) / 2
  }
}

// 监听窗口大小变化
onMounted(() => {
  window.addEventListener('resize', initDialogPosition)
  initDialogPosition()
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await UserAPI.getUserInfo()
    userInfo.value = response.data
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await authAPI.logout()
    localStorage.removeItem('token')
    await router.push('/login')
    ElMessage.success('退出成功')
  } catch (error) {
    ElMessage.error('退出失败')
  }
}

// 获取聊天列表
const getChatList = async () => {
  try {
    const response = await UserAPI.getChatList()
    chatList.value = response.data
  } catch (error) {
    ElMessage.error('获取聊天列表失败')
  }
}

// 发送消息
const sendMessage = (content) => {
  if (!activeChat.value) return
  
  // TODO: 实现发送消息的逻辑
  messages.value.push({
    content,
    timestamp: new Date().toLocaleTimeString(),
    isSelf: true
  })
}

// 选择聊天
const selectChat = (chat) => {
  activeChat.value = chat
  // TODO: 获取聊天记录
}

// 添加好友
const handleAddFriend = async () => {
  try {
    await UserAPI.addFriend(friendUsername.value)
    ElMessage.success('好友请求已发送')
    addFriendDialogVisible.value = false
    friendUsername.value = ''
  } catch (error) {
    ElMessage.error('添加好友失败')
  }
}

// 创建群聊
const handleCreateGroup = async () => {
  try {
    await UserAPI.createGroup(groupForm.value)
    ElMessage.success('群聊创建成功')
    createGroupDialogVisible.value = false
    groupForm.value = { name: '', description: '' }
    await getChatList()
  } catch (error) {
    ElMessage.error('创建群聊失败')
  }
}

// 处理弹窗显示
const showDialog = (type) => {
  initDialogPosition()
  if (type === 'friend') {
    addFriendDialogVisible.value = true
  } else {
    createGroupDialogVisible.value = true
  }
}

// 页面加载时获取用户信息和聊天列表
getUserInfo()
getChatList()
</script>

<template>
  <div class="home-container">
    <!-- 左侧导航栏 -->
    <NavBar 
      :user-info="userInfo"
      @logout="handleLogout"
    />

    <!-- 中间聊天列表 -->
    <ChatList
      :chat-list="chatList"
      :active-chat="activeChat"
      @select-chat="selectChat"
      @add-friend="showDialog('friend')"
      @create-group="showDialog('group')"
    />

    <!-- 右侧聊天窗口 -->
    <ChatWindow
      :active-chat="activeChat"
      :messages="messages"
      :user-info="userInfo"
      @send-message="sendMessage"
    />

    <!-- 添加好友弹窗 -->
    <el-dialog
      v-model="addFriendDialogVisible"
      title="添加好友"
      width="400px"
      :modal="false"
      :close-on-click-modal="false"
      draggable
      :style="{
        position: 'fixed',
        left: dialogPosition.x + 'px',
        top: dialogPosition.y + 'px',
        margin: 0
      }"
    >
      <div class="dialog-content">
        <el-input v-model="friendUsername" placeholder="请输入好友账号" />
        <div class="dialog-footer">
          <el-button @click="addFriendDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddFriend">添加</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 创建群聊弹窗 -->
    <el-dialog
      v-model="createGroupDialogVisible"
      title="创建群聊"
      width="400px"
      :modal="false"
      :close-on-click-modal="false"
      draggable
      :style="{
        position: 'fixed',
        left: dialogPosition.x + 'px',
        top: dialogPosition.y + 'px',
        margin: 0
      }"
    >
      <div class="dialog-content">
        <el-input v-model="groupForm.name" placeholder="群聊名称" />
        <el-input
          v-model="groupForm.description"
          type="textarea"
          :rows="3"
          placeholder="群聊描述"
          style="margin-top: 16px"
        />
        <div class="dialog-footer">
          <el-button @click="createGroupDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCreateGroup">创建</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.home-container {
  height: 100vh;
  display: flex;
  background-color: #f5f5f5;
}

.dialog-content {
  padding: 20px 0;
}

.dialog-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-dialog) {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.el-dialog__header) {
  margin: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #e6e6e6;
  cursor: move;
}

:deep(.el-dialog__headerbtn) {
  top: 16px;
}

:deep(.el-dialog__body) {
  padding: 0 20px;
}
</style> 