<script setup>
import { ref, onMounted } from 'vue'
import { authAPI } from '../api/AuthAPI.js'
import { UserAPI } from "../api/UserAPI.js"
import { ElMessage } from 'element-plus'
import router from '../router'
import NavBar from '../components/NavBar.vue'
import ChatList from '../components/ChatList.vue'
import ChatWindow from '../components/ChatWindow.vue'
import { Search, Male, MoreFilled, Message, Phone, VideoCamera } from '@element-plus/icons-vue'

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
const searchResult = ref(null)
const addFriendRemark = ref('')
const hasSearched = ref(false)

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
      title="添加朋友"
      width="380px"
      height="200px"
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
        <div class="search-box">
          <el-input 
            v-model="friendUsername" 
            placeholder="微信号/手机号" 
            class="search-input"
            clearable
          >
            <template #append>
              <el-button type="success" class="search-btn" @click="searchUser">搜索</el-button>
            </template>
          </el-input>
        </div>

        <!-- 搜索结果区域 -->
        <template v-if="searchResult">
          <div class="user-basic-info">
            <el-avatar 
              :size="45" 
              :src="searchResult.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
              class="user-avatar"
            />
            <div class="user-info">
              <div class="nickname">
                {{ searchResult.nickname }}
                <el-icon class="gender-icon"><Male /></el-icon>
              </div>
              <div class="wx-id">微信号：{{ searchResult.username }}</div>
            </div>
            <el-dropdown trigger="click" class="more-actions" v-if="searchResult.status !== 'NOT_FRIEND'">
              <el-button text><el-icon><MoreFilled /></el-icon></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>设置备注和标签</el-dropdown-item>
                  <el-dropdown-item>设置朋友权限</el-dropdown-item>
                  <el-dropdown-item>把他推荐给朋友</el-dropdown-item>
                  <el-dropdown-item>设为星标朋友</el-dropdown-item>
                  <el-dropdown-item>加入黑名单</el-dropdown-item>
                  <el-dropdown-item class="danger">删除联系人</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>

          <div class="user-detail">
            <div class="detail-item">
              <div class="label">备注</div>
              <div class="value">
                <el-input 
                  v-model="addFriendRemark" 
                  placeholder="添加备注名" 
                  class="remark-input"
                />
              </div>
            </div>
            <div class="detail-item">
              <div class="label">朋友圈</div>
              <div class="value"></div>
            </div>
            <div class="detail-item">
              <div class="label">个性签名</div>
              <div class="value signature">{{ searchResult.signature || '暂无签名' }}</div>
            </div>
            <div class="detail-item">
              <div class="label">来源</div>
              <div class="value source">通过搜索微信号添加</div>
            </div>
          </div>

          <!-- 添加好友按钮 -->
          <div class="add-friend-action" v-if="searchResult.status === 'NOT_FRIEND'">
            <el-button type="primary" @click="handleAddFriend" class="add-btn">添加到通讯录</el-button>
          </div>
        </template>

        <!-- 无搜索结果提示 -->
        <template v-else-if="hasSearched">
          <div class="no-result">
            <el-empty description="该用户不存在" />
          </div>
        </template>
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

.search-box {
  padding: 8px;
  background-color: #f5f5f5;
}

.search-input {
  :deep(.el-input__wrapper) {
    background-color: #fff;
    border-radius: 4px;
    height: 32px;
  }
  
  :deep(.el-input-group__append) {
    padding: 0;
  }
}

.search-btn {
  border: none;
  padding: 6px 12px;
  border-radius: 0 4px 4px 0;
  height: 32px;
}

.search-result {
  padding: 0;
}

.user-basic-info {
  padding: 12px;
  display: flex;
  align-items: flex-start;
  border-bottom: 1px solid #eee;
  position: relative;
}

.user-avatar {
  margin-right: 10px;
}

.user-info {
  flex: 1;
}

.nickname {
  font-size: 15px;
  font-weight: 400;
  color: #000;
  display: flex;
  align-items: center;
  margin-bottom: 2px;
}

.gender-icon {
  color: #10aeff;
  margin-left: 4px;
}

.wx-id {
  font-size: 13px;
  color: #888;
}

.more-actions {
  position: absolute;
  right: 10px;
  top: 10px;
}

.user-detail {
  padding: 0 15px;
}

.detail-item {
  display: flex;
  padding: 10px 12px;
  border-bottom: 1px solid #eee;
}

.detail-item .label {
  width: 70px;
  color: #888;
  font-size: 13px;
}

.detail-item .value {
  flex: 1;
  font-size: 13px;
}

.remark-input {
  :deep(.el-input__wrapper) {
    padding: 0;
    box-shadow: none;
  }
}

.signature, .source {
  color: #353535;
}

.action-buttons {
  display: flex;
  justify-content: space-around;
  padding: 15px;
  border-top: 1px solid #eee;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #353535;
  font-size: 12px;
  
  .el-icon {
    font-size: 24px;
    margin-bottom: 5px;
  }
}

:deep(.el-dropdown-menu__item.danger) {
  color: #ff4d4f;
}

:deep(.el-dialog) {
  border-radius: 4px;
  overflow: hidden;
  height: auto;
  max-height: 500px;
}

:deep(.el-dialog__header) {
  margin: 0;
  padding: 10px 12px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #eee;
}

:deep(.el-dialog__title) {
  font-size: 15px;
  font-weight: normal;
}

:deep(.el-dialog__body) {
  padding: 0;
}

:deep(.el-dialog__headerbtn) {
  top: 12px;
  right: 12px;
}

.add-friend-action {
  padding: 16px;
  text-align: center;
  border-top: 1px solid #eee;
}

.add-btn {
  width: 200px;
}

.no-result {
  padding: 40px 0;
  text-align: center;
}
</style> 