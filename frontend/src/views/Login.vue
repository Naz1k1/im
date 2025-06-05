<script setup>
  import {ref} from "vue";
  import {authApi} from "../api/authApi.js";
  import router from "../router/index.js";
  import {ElMessage} from "element-plus";

  const form = ref({
    username:'',
    password:''
  })

  const rules = {
    username: [{ required: true, message: '请输入帐号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
  }

  const loading = ref(false)
  const loginForm = ref(null)

  const loginHandle = async () => {
    try {
      await loginForm.value.validate()
      loading.value = true
      await authApi.login(form.value.username,form.value.password)

      await router.push({ path:'/' })
      ElMessage.success('登录成功')
    }catch (e) {

    }
  }

</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">在此登录</h2>
      <el-form
          ref="loginForm"
          :model = 'form'
          :rules = 'rules'
          @keyup.enter = 'loginHandle'
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
              show-password/>
        </el-form-item>
        <el-button
            class="login-btn"
            type="primary"
            @click = 'loginHandle'
            :loading="loading"
        >登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
}

.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.login-btn {
  width: 100%;
  margin-top: 10px;
}
</style>