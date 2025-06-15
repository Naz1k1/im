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
          :src= "'http://localhost:8080/static/image/avatar/defalut-avatar.svg'"
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
  width: 72px;
  background-color: var(--color-bg-main);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0;
  height: 100%;
}

.nav-header {
  margin-top: 64px;
  margin-bottom: 32px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  width: 100%;
}

.nav-header :deep(.el-avatar) {
  width: 56px !important;
  height: 56px !important;
  border-radius: 16px !important;
  border: 2px solid #fff !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.10);
  box-sizing: border-box;
  background: var(--color-bg-secondary);
  transition: box-shadow 0.2s;
}

.nav-header :deep(.el-avatar):hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.18);
}

.nav-menu {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  flex: 1;
}

.nav-main {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  margin-top: 0;
  width: 100%;
}

.nav-item {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #7c7c7c;
  cursor: pointer;
  border-radius: 12px;
  transition: all 0.2s;
  margin-bottom: 2px;
}

.nav-item:hover {
  background-color: var(--color-bg-secondary);
  color: var(--color-primary);
}

.nav-item.active {
  background-color: var(--color-bg-secondary);
  color: var(--color-primary);
  position: relative;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  border-radius: 2px;
  background: var(--color-primary);
}

.nav-icon {
  width: 28px;
  height: 28px;
  filter: invert(60%);
  transition: all 0.2s;
}

.nav-item:hover .nav-icon,
.nav-item.active .nav-icon {
  filter: invert(100%) sepia(1) saturate(5) hue-rotate(80deg);
}

.nav-bottom {
  margin-top: auto;
  display: flex;
  justify-content: center;
  padding-bottom: 24px;
  width: 100%;
}
</style> 