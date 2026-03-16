<template>
  <div class="login-page">
    <div class="login-panel">
      <div class="login-copy">
        <div class="login-badge">B/S · Spring Boot · Vue2</div>
        <h1>汽车租赁管理系统</h1>
        <p>面向车辆管理、客户管理、订单流转和首页广告位运营的一体化后台。</p>
      </div>

      <el-form ref="form" :model="form" :rules="rules" class="login-form">
        <h2>后台登录</h2>
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="submit"
          />
        </el-form-item>
        <el-button type="primary" class="login-button" :loading="loading" @click="submit">登录系统</el-button>
        <div class="tips">默认账号：admin / password</div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from "../api/auth";
import { setTokens } from "../utils/storage";

export default {
  name: "LoginPage",
  data() {
    return {
      loading: false,
      form: {
        username: "admin",
        password: "password"
      },
      rules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      }
    };
  },
  methods: {
    submit() {
      this.$refs.form.validate(async valid => {
        if (!valid) {
          return;
        }
        this.loading = true;
        try {
          const res = await login(this.form);
          setTokens(res.data.accessToken, res.data.refreshToken);
          this.$message.success("登录成功");
          this.$router.replace("/dashboard");
        } finally {
          this.loading = false;
        }
      });
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

.login-form {
  padding: 56px 44px;
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

  .login-form {
    padding: 32px 24px;
  }
}
</style>
