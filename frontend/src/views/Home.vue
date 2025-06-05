<script setup>
import { ref } from 'vue'
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
    />

    <!-- 右侧聊天窗口 -->
    <ChatWindow
      :active-chat="activeChat"
      :messages="messages"
      :user-info="userInfo"
      @send-message="sendMessage"
    />
  </div>
</template>

<style scoped>
.home-container {
  height: 100vh;
  display: flex;
  background-color: #f5f5f5;
}
</style> 