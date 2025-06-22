<script setup>
import {ref} from "vue";
import {authAPI} from "../api/AuthAPI.js";
import router from "../router/index.js";
import {ElMessage} from "element-plus";

const form = ref({
  username: '',
  password: '',
  nickname: '',
  email:''
})

const rules = {
  username: [{ required: true, message: '请输入帐号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
}

const loading = ref(false)
const registerForm = ref(null)

const registerHandle = async () => {
  try {
    await registerForm.value.validate()
    loading.value = true
    await authAPI.register(form.value)
    ElMessage.success('注册成功')
    await router.push({ path:'/login' })
  } catch (e) {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form
          ref="registerForm"
          :model='form'
          :rules='rules'
          @keyup.enter='registerHandle'
      >
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              placeholder="请输入帐号"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              placeholder="请输入密码"
              show-password
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
              v-model="form.nickname"
              placeholder="请输入昵称"
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input
              v-model="form.email"
              placeholder="请输入邮箱"
          />
        </el-form-item>
        <el-button
            class="register-btn"
            type="primary"
            @click='registerHandle'
            :loading="loading"
        >注册</el-button>
        <div class="login-link">
          已有账号？<a href="#" @click="goToLogin">立即登录</a>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
}

.register-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.register-btn {
  width: 100%;
  margin-top: 10px;
}

.login-link {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
}

.login-link a {
  color: #409EFF;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style> 