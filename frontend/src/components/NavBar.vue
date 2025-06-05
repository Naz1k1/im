<script setup>
import { ref } from 'vue'
import chatIcon from '../assets/image/chat.svg'
import contactIcon from '../assets/image/contact.svg'
import moreIcon from '../assets/image/more.svg'

const props = defineProps({
  userInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['nav-change', 'logout'])
const activeNav = ref('chat')

const handleNavClick = (nav) => {
  activeNav.value = nav
  emit('nav-change', nav)
}
</script>

<template>
  <div class="nav-bar">
    <!-- 用户头像 -->
    <div class="nav-header">
      <el-avatar
          :size="40"
          :src="userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
          @click="emit('logout')"
      />
    </div>
    <!-- 导航菜单 -->
    <div class="nav-menu">
      <!-- 主要导航项 -->
      <div class="nav-main">
        <div
            class="nav-item"
            :class="{ active: activeNav === 'chat' }"
            @click="handleNavClick('chat')"
        >
          <img :src="chatIcon" class="nav-icon" alt="chat"/>
        </div>
        <div
            class="nav-item"
            :class="{ active: activeNav === 'contacts' }"
            @click="handleNavClick('contacts')"
        >
          <img :src="contactIcon" class="nav-icon" alt="contacts"/>
        </div>
      </div>
      <!-- 底部导航项 -->
      <div class="nav-bottom">
        <div
            class="nav-item"
            :class="{ active: activeNav === 'more' }"
            @click="handleNavClick('more')"
        >
          <img :src="moreIcon" class="nav-icon" alt="more"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.nav-bar {
  width: 60px;
  background-color: #2f2f2f;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  height: 100%; /* 确保导航栏占满高度 */
}

.nav-header {
  margin-bottom: 20px;
  cursor: pointer;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1; /* 让导航菜单占据剩余空间 */
  width: 100%; /* 确保宽度占满 */
}

.nav-main {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
}

.nav-bottom {
  margin-top: auto; /* 将底部导航推到底部 */
  display: flex;
  justify-content: center;
  padding-bottom: 20px; /* 底部间距 */
}

.nav-item {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #7c7c7c;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #3e3e3e;
  color: #fff;
}

.nav-item.active {
  background-color: #3e3e3e;
  color: #fff;
}

.nav-icon {
  width: 24px;
  height: 24px;
  filter: invert(60%);
  transition: all 0.3s;
}

.nav-item:hover .nav-icon {
  filter: invert(100%);
}

.nav-item.active .nav-icon {
  filter: invert(100%);
}
</style> 