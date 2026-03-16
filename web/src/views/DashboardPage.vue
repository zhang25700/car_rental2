<template>
  <div class="dashboard">
    <div class="hero">
      <div>
        <div class="hero-kicker">运营总览</div>
        <h2>租赁业务数据与首页推荐内容集中展示</h2>
        <p>热门车辆和广告位数据直接来自 Redis 缓存，适合作为首页低延迟展示区域。</p>
      </div>
      <div class="hero-mark">CAR</div>
    </div>

    <div class="stat-grid">
      <div class="page-card stat-card">
        <div class="stat-label">热门车辆</div>
        <div class="stat-value">{{ hotVehicles.length }}</div>
      </div>
      <div class="page-card stat-card">
        <div class="stat-label">首页广告</div>
        <div class="stat-value">{{ banners.length }}</div>
      </div>
      <div class="page-card stat-card">
        <div class="stat-label">系统状态</div>
        <div class="stat-value">稳定</div>
      </div>
    </div>

    <div class="dashboard-grid">
      <div class="page-card">
        <h3 class="section-title">热点车辆</h3>
        <el-table :data="hotVehicles">
          <el-table-column prop="vehicleNo" label="车牌号" />
          <el-table-column prop="brand" label="品牌" />
          <el-table-column prop="model" label="型号" />
          <el-table-column prop="dailyPrice" label="日租金" />
          <el-table-column prop="status" label="状态" />
        </el-table>
      </div>

      <div class="page-card">
        <h3 class="section-title">首页广告位</h3>
        <el-timeline>
          <el-timeline-item
            v-for="item in banners"
            :key="item.id"
            :timestamp="item.updatedAt || item.createdAt"
            placement="top"
          >
            <div class="timeline-title">{{ item.title }}</div>
            <div class="timeline-desc">{{ item.redirectUrl || '无跳转地址' }}</div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script>
import { getActiveBanners } from "../api/banner";
import { getHotVehicles } from "../api/vehicle";

export default {
  name: "DashboardPage",
  data() {
    return {
      hotVehicles: [],
      banners: []
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    async loadData() {
      const [vehicleRes, bannerRes] = await Promise.all([getHotVehicles(), getActiveBanners()]);
      this.hotVehicles = vehicleRes.data || [];
      this.banners = bannerRes.data || [];
    }
  }
};
</script>

<style scoped>
.hero {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 30px;
  border-radius: 24px;
  margin-bottom: 22px;
  background:
    linear-gradient(140deg, rgba(15, 118, 110, 0.92), rgba(23, 50, 77, 0.95)),
    linear-gradient(135deg, #0f766e, #17324d);
  color: #fff;
}

.hero-kicker {
  display: inline-block;
  margin-bottom: 10px;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
}

.hero h2 {
  margin: 0 0 12px;
  font-size: 30px;
}

.hero p {
  max-width: 560px;
  color: rgba(255, 255, 255, 0.82);
}

.hero-mark {
  font-size: 68px;
  font-weight: 800;
  color: rgba(255, 255, 255, 0.12);
}

.stat-grid,
.dashboard-grid {
  display: grid;
  gap: 20px;
}

.stat-grid {
  grid-template-columns: repeat(3, 1fr);
  margin-bottom: 20px;
}

.dashboard-grid {
  grid-template-columns: 1.15fr 0.85fr;
}

.stat-card {
  min-height: 130px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stat-label {
  color: var(--text-regular);
}

.stat-value {
  margin-top: 8px;
  font-size: 34px;
  font-weight: 800;
}

.timeline-title {
  font-weight: 700;
}

.timeline-desc {
  margin-top: 6px;
  color: var(--text-regular);
  word-break: break-all;
}

@media (max-width: 960px) {
  .stat-grid,
  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .hero {
    align-items: flex-start;
  }

  .hero-mark {
    display: none;
  }
}
</style>
