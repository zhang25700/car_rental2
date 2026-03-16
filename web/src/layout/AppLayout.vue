<template>
  <div class="layout-shell">
    <aside class="layout-sidebar">
      <div class="brand">
        <div class="brand-mark">CR</div>
        <div>
          <div class="brand-title">汽车租赁管理系统</div>
          <div class="brand-sub">管理端</div>
        </div>
      </div>

      <el-menu
        :default-active="$route.path"
        background-color="transparent"
        text-color="#d6e4ef"
        active-text-color="#ffffff"
        @select="handleSelect"
      >
        <el-menu-item index="/admin/dashboard">首页概览</el-menu-item>
        <el-menu-item index="/admin/vehicles">车辆管理</el-menu-item>
        <el-menu-item index="/admin/customers">客户管理</el-menu-item>
        <el-menu-item index="/admin/orders">订单管理</el-menu-item>
        <el-menu-item index="/admin/banners">广告管理</el-menu-item>
        <el-menu-item index="/admin/users">用户管理</el-menu-item>
      </el-menu>
    </aside>

    <main class="layout-main">
      <header class="layout-header">
        <div>
          <div class="header-title">{{ titleMap[$route.path] || "管理后台" }}</div>
          <div class="header-subtitle">围绕车辆、客户、订单、广告和账号的日常管理</div>
        </div>
        <el-button type="danger" plain @click="logout">退出登录</el-button>
      </header>

      <transition name="fade-rise" mode="out-in">
        <router-view />
      </transition>
    </main>
  </div>
</template>

<script>
import { clearTokens } from "../utils/storage";

export default {
  name: "AppLayout",
  data() {
    return {
      titleMap: {
        "/admin/dashboard": "首页概览",
        "/admin/vehicles": "车辆管理",
        "/admin/customers": "客户管理",
        "/admin/orders": "订单管理",
        "/admin/banners": "广告管理",
        "/admin/users": "用户管理"
      }
    };
  },
  methods: {
    handleSelect(index) {
      this.$router.push(index);
    },
    logout() {
      clearTokens();
      this.$router.replace("/login");
    }
  }
};
</script>

<style scoped>
.layout-shell { min-height: 100vh; display: grid; grid-template-columns: 260px 1fr; }
.layout-sidebar { padding: 28px 20px; background: linear-gradient(180deg, rgba(10, 38, 58, 0.96), rgba(13, 90, 91, 0.94)), linear-gradient(135deg, #0f172a, #0f766e); color: #fff; }
.brand { display: flex; align-items: center; gap: 12px; margin-bottom: 28px; }
.brand-mark { width: 46px; height: 46px; border-radius: 14px; display: grid; place-items: center; font-weight: 800; background: linear-gradient(135deg, #f59e0b, #f97316); color: #fff; }
.brand-title { font-size: 18px; font-weight: 700; }
.brand-sub { margin-top: 4px; font-size: 12px; color: rgba(214, 228, 239, 0.75); }
.layout-main { padding: 24px; }
.layout-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 22px; }
.header-title { font-size: 28px; font-weight: 800; }
.header-subtitle { margin-top: 6px; color: var(--text-regular); }
::v-deep .el-menu { border-right: none; }
::v-deep .el-menu-item { height: 48px; line-height: 48px; border-radius: 12px; margin-bottom: 10px; }
::v-deep .el-menu-item.is-active { background: rgba(255, 255, 255, 0.12) !important; }
@media (max-width: 960px) { .layout-shell { grid-template-columns: 1fr; } .layout-sidebar { padding-bottom: 8px; } }
</style>
