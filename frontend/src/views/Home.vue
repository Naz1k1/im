<script setup>
import { ref, onMounted } from 'vue'
import { authAPI } from '../api/AuthAPI.js'
import { UserAPI } from "../api/UserAPI.js"
import { ElMessage } from 'element-plus'
import router from '../router'
import NavBar from '../components/NavBar.vue'
import ChatList from '../components/ChatList.vue'
import ChatWindow from '../components/ChatWindow.vue'
import ContactList from '../components/ContactList.vue'
import FriendRequestDetail from '../components/FriendRequestDetail.vue'
import FriendProfile from '../components/FriendProfile.vue'
import { Search, Male, MoreFilled, Message, Phone, VideoCamera } from '@element-plus/icons-vue'

const userInfo = ref({
  username: '',
  nickname: ''
})

const activeNav = ref('chat')
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
const searchResult = ref(null)
const addFriendRemark = ref('')
const hasSearched = ref(false)

const selectedFriendRequest = ref(null)
const selectedFriend = ref(null)

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
    // const response = await UserAPI.getChatList()
    // chatList.value = response.data
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
    if (!searchResult.value || !searchResult.value.userId) {
      ElMessage.error('用户信息不完整')
      return
    }
    await UserAPI.addFriend(searchResult.value.userId, addFriendRemark.value)
    ElMessage.success('好友请求已发送')
    addFriendDialogVisible.value = false
    friendUsername.value = ''
    addFriendRemark.value = ''
    searchResult.value = null
    hasSearched.value = false
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

// 搜索用户
const searchUser = async () => {
  try {
    const response = await UserAPI.searchUser(friendUsername.value)
    if (response.data.code === 200 && response.data.data) {
      searchResult.value = response.data.data
      hasSearched.value = false
    } else {
      searchResult.value = null
      hasSearched.value = true
    }
  } catch (error) {
    ElMessage.error('搜索用户失败')
    searchResult.value = null
    hasSearched.value = true
  }
}

// 监听导航栏切换
const handleNavChange = (nav) => {
  activeNav.value = nav
}

const handleSelectFriendRequest = (item) => {
  selectedFriendRequest.value = item
}

const handleSelectFriend = (item) => {
  selectedFriend.value = item
  selectedFriendRequest.value = null // 切换时清空好友请求详情
}

const handleStartChat = (friend) => {
  activeChat.value = friend
  selectedFriend.value = null
  selectedFriendRequest.value = null
  // 可选：加载该好友的聊天记录
}

// 页面加载时获取用户信息和聊天列表
getUserInfo()
getChatList()
</script>

<template>
  <div class="home-container">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <NavBar 
        :user-info="userInfo"
        @logout="handleLogout"
        @nav-change="handleNavChange"
      />
    </div>

    <!-- 中间聊天列表或通讯录 -->
    <div class="chatlist-panel">
      <ChatList
        v-if="activeNav === 'chat'"
        :chat-list="chatList"
        :active-chat="activeChat"
        @select-chat="selectChat"
        @add-friend="showDialog('friend')"
        @create-group="showDialog('group')"
      />
      <ContactList
        v-else-if="activeNav === 'contacts'"
        @select-friend-request="handleSelectFriendRequest"
        @select-friend="handleSelectFriend"
      />
    </div>

    <!-- 右侧聊天窗口 -->
    <div class="chatwindow-panel">
      <FriendRequestDetail
        v-if="selectedFriendRequest"
        :request="selectedFriendRequest"
      />
      <FriendProfile
        v-else-if="selectedFriend"
        :friend="selectedFriend"
        @start-chat="handleStartChat"
      />
      <ChatWindow
        v-else
        :active-chat="activeChat"
        :messages="messages"
        :user-info="userInfo"
        @send-message="sendMessage"
      />
    </div>
  </div>
</template>

<style scoped>
.home-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  background: var(--color-bg-main);
  font-family: var(--font-family);
  overflow: hidden;
}
.sidebar {
  width: 72px;
  background: var(--color-bg-main);
  border-right: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 2;
}
.chatlist-panel {
  width: 320px;
  background: var(--color-bg-secondary);
  border-right: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  z-index: 1;
}
.chatwindow-panel {
  flex: 1;
  background: var(--color-bg-chat);
  display: flex;
  flex-direction: column;
  min-width: 0;
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: #222;
  border-radius: 4px;
}

/* 去除element弹窗圆角和阴影 */
:deep(.el-dialog) {
  border-radius: var(--radius-main);
  box-shadow: none;
  background: var(--color-bg-secondary);
  color: var(--color-text-main);
}
:deep(.el-dialog__header) {
  margin: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--color-border);
  cursor: move;
  background: var(--color-bg-secondary);
}
:deep(.el-dialog__headerbtn) {
  top: 16px;
}

:deep(.el-dialog__title) {
  font-size: 15px;
  font-weight: normal;
  color: var(--color-text-main);
}
:deep(.el-dialog__body) {
  padding: 0;
  background: var(--color-bg-secondary);
}
:deep(.el-dialog__headerbtn) {
  top: 12px;
  right: 12px;
}
</style> 