<template>
  <div class="user-shell">
    <header class="user-header">
      <div class="user-brand">
        <div class="user-logo">租</div>
        <div>
          <div class="user-title">汽车租赁用户中心</div>
          <div class="user-subtitle">查看车辆与活动信息</div>
        </div>
      </div>
      <div class="user-actions">
        <el-button type="text" @click="$router.push('/user/home')">首页</el-button>
        <el-button type="text" @click="$router.push('/user/vehicles')">可租车辆</el-button>
        <el-button type="text" @click="$router.push('/user/banners')">优惠活动</el-button>
        <el-button type="danger" plain @click="logout">退出登录</el-button>
      </div>
    </header>

    <main class="user-main">
      <transition name="fade-rise" mode="out-in">
        <router-view />
      </transition>
    </main>
  </div>
</template>

<script>
import { clearTokens } from "../utils/storage";

export default {
  name: "UserLayout",
  methods: {
    logout() {
      clearTokens();
      this.$router.replace("/login");
    }
  }
};
</script>

<style scoped>
.user-shell { min-height: 100vh; background: linear-gradient(180deg, #f7fafc 0%, #eef3f7 100%); }
.user-header { display: flex; justify-content: space-between; align-items: center; padding: 20px 28px; background: #fff; box-shadow: 0 10px 30px rgba(17, 38, 60, 0.06); }
.user-brand { display: flex; align-items: center; gap: 12px; }
.user-logo { width: 44px; height: 44px; border-radius: 14px; display: grid; place-items: center; background: linear-gradient(135deg, #0f766e, #17324d); color: #fff; font-weight: 700; }
.user-title { font-size: 20px; font-weight: 700; color: var(--text-primary); }
.user-subtitle { margin-top: 4px; color: var(--text-regular); }
.user-actions { display: flex; align-items: center; gap: 8px; }
.user-main { padding: 24px; max-width: 1200px; margin: 0 auto; }
@media (max-width: 960px) { .user-header { flex-direction: column; align-items: flex-start; gap: 12px; } .user-actions { flex-wrap: wrap; } }
</style>
