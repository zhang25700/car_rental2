<template>
  <div class="login-page">
    <div class="login-panel">
      <div class="login-copy">
        <div class="login-badge">租赁业务后台</div>
        <h1>汽车租赁管理系统</h1>
        <p>用于维护车辆信息、客户资料、租赁订单、广告内容以及账号信息。</p>
      </div>

      <div class="login-form-wrap">
        <div class="form-switch">
          <button :class="{ active: mode === 'login' }" @click="mode = 'login'">登录</button>
          <button :class="{ active: mode === 'register' }" @click="mode = 'register'">注册</button>
        </div>

        <el-form
          v-if="mode === 'login'"
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <h2>账号登录</h2>
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              @keyup.enter.native="submitLogin"
            />
          </el-form-item>
          <el-button type="primary" class="login-button" :loading="loading" @click="submitLogin">登录系统</el-button>
          <div class="tips">默认管理员账号：admin / password</div>
        </el-form>

        <el-form
          v-else
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="login-form"
        >
          <h2>注册账号</h2>
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user" />
          </el-form-item>
          <el-form-item prop="realName">
            <el-input v-model="registerForm.realName" placeholder="请输入姓名" prefix-icon="el-icon-edit" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
            />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              prefix-icon="el-icon-lock"
              @keyup.enter.native="submitRegister"
            />
          </el-form-item>
          <el-button type="primary" class="login-button" :loading="loading" @click="submitRegister">提交注册</el-button>
          <div class="tips">注册成功后将进入用户端</div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { login, register } from "../api/auth";
import { setTokens } from "../utils/storage";

export default {
  name: "LoginPage",
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请再次输入密码"));
        return;
      }
      if (value !== this.registerForm.password) {
        callback(new Error("两次输入的密码不一致"));
        return;
      }
      callback();
    };

    return {
      mode: "login",
      loading: false,
      loginForm: {
        username: "admin",
        password: "password"
      },
      registerForm: {
        username: "",
        realName: "",
        password: "",
        confirmPassword: ""
      },
      loginRules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },
      registerRules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        realName: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        confirmPassword: [{ validator: validateConfirmPassword, trigger: "blur" }]
      }
    };
  },
  methods: {
    submitLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) {
          return;
        }
        this.loading = true;
        try {
          const res = await login(this.loginForm);
          setTokens(res.data.accessToken, res.data.refreshToken, res.data.role);
          this.$message.success("登录成功");
          this.$router.replace(this.isAdminRole(res.data.role) ? "/admin/dashboard" : "/user/home");
        } finally {
          this.loading = false;
        }
      });
    },
    submitRegister() {
      this.$refs.registerFormRef.validate(async valid => {
        if (!valid) {
          return;
        }
        this.loading = true;
        try {
          await register({
            username: this.registerForm.username,
            realName: this.registerForm.realName,
            password: this.registerForm.password
          });
          this.$message.success("注册成功，请登录");
          this.mode = "login";
          this.loginForm.username = this.registerForm.username;
          this.loginForm.password = "";
          this.registerForm = {
            username: "",
            realName: "",
            password: "",
            confirmPassword: ""
          };
        } finally {
          this.loading = false;
        }
      });
    },
    isAdminRole(role) {
      return ["ADMIN", "OPERATOR", "STAFF"].includes(role);
    }
  }
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 24px;
  background:
    radial-gradient(circle at 0% 0%, rgba(245, 158, 11, 0.18), transparent 28%),
    radial-gradient(circle at 100% 100%, rgba(15, 118, 110, 0.18), transparent 28%),
    linear-gradient(135deg, #f8fbfd, #ebf2f7);
}

.login-panel {
  width: min(1040px, 100%);
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  overflow: hidden;
  border-radius: 28px;
  box-shadow: 0 25px 60px rgba(14, 30, 37, 0.12);
  background: #fff;
}

.login-copy {
  padding: 56px;
  color: #f8fafc;
  background:
    linear-gradient(160deg, rgba(11, 91, 85, 0.86), rgba(15, 23, 42, 0.92)),
    linear-gradient(135deg, #0f766e, #0f172a);
}

.login-copy h1 {
  margin: 20px 0 18px;
  font-size: 42px;
}

.login-copy p {
  max-width: 420px;
  line-height: 1.8;
  color: rgba(248, 250, 252, 0.82);
}

.login-badge {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
}

.login-form-wrap {
  padding: 40px 44px;
}

.form-switch {
  display: inline-flex;
  padding: 4px;
  margin-bottom: 18px;
  border-radius: 999px;
  background: #eef3f7;
}

.form-switch button {
  border: none;
  background: transparent;
  padding: 10px 22px;
  border-radius: 999px;
  cursor: pointer;
  color: var(--text-regular);
}

.form-switch button.active {
  background: #ffffff;
  color: var(--text-primary);
  box-shadow: 0 6px 18px rgba(17, 38, 60, 0.08);
}

.login-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-form h2 {
  margin: 0 0 24px;
  font-size: 28px;
}

.login-button {
  width: 100%;
  height: 46px;
  margin-top: 8px;
  background: linear-gradient(135deg, #0f766e, #0b5b55);
  border: none;
}

.tips {
  margin-top: 14px;
  text-align: center;
  color: var(--text-regular);
}

@media (max-width: 960px) {
  .login-panel {
    grid-template-columns: 1fr;
  }

  .login-copy {
    padding: 36px 30px;
  }

  .login-form-wrap {
    padding: 32px 24px;
  }
}
</style>
