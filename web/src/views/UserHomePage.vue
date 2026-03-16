<template>
  <div>
    <div class="page-card user-hero">
      <div>
        <div class="hero-kicker">欢迎使用</div>
        <h2>在线查看可租车辆和首页优惠活动</h2>
        <p>首页广告位采用轮播展示，用户进入后可以先浏览活动，再查看可租车辆并提交订单。</p>
      </div>
      <el-button type="primary" @click="$router.push('/user/vehicles')">查看车辆</el-button>
    </div>

    <div class="page-card banner-panel">
      <h3 class="section-title">首页广告</h3>
      <el-carousel v-if="banners.length" height="320px" indicator-position="outside">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <div class="banner-slide">
            <img
              v-if="item.imageUrl && !imageErrors[item.id]"
              :src="item.imageUrl"
              :alt="item.title"
              class="banner-image"
              @error="markImageError(item.id)"
            />
            <div v-else class="banner-fallback">
              <div class="banner-fallback-title">{{ item.title }}</div>
            </div>
            <div class="banner-overlay">
              <div class="banner-label">首页广告位</div>
              <div class="banner-title">{{ item.title }}</div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      <div v-else class="banner-empty">暂无活动内容</div>
    </div>

    <div class="page-card">
      <h3 class="section-title">热门车辆</h3>
      <el-table :data="hotVehicles">
        <el-table-column prop="brand" label="品牌" />
        <el-table-column prop="model" label="型号" />
        <el-table-column prop="dailyPrice" label="日租金" />
        <el-table-column label="状态">
          <template slot-scope="{ row }">{{ statusText(row.status) }}</template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getActiveBanners } from "../api/banner";
import { getHotVehicles } from "../api/vehicle";

export default {
  name: "UserHomePage",
  data() {
    return {
      hotVehicles: [],
      banners: [],
      imageErrors: {}
    };
  },
  async created() {
    const [vehicleRes, bannerRes] = await Promise.all([getHotVehicles(), getActiveBanners()]);
    this.hotVehicles = vehicleRes.data || [];
    this.banners = bannerRes.data || [];
  },
  methods: {
    statusText(status) {
      return { AVAILABLE: "可租赁", RENTED: "租赁中", MAINTENANCE: "维护中" }[status] || status;
    },
    markImageError(id) {
      this.$set(this.imageErrors, id, true);
    }
  }
};
</script>

<style scoped>
.user-hero {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background: linear-gradient(135deg, rgba(15, 118, 110, 0.92), rgba(23, 50, 77, 0.95));
  color: #fff;
}

.hero-kicker {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  margin-bottom: 12px;
}

.user-hero h2 {
  margin: 0 0 12px;
  font-size: 30px;
}

.banner-panel {
  margin-bottom: 20px;
}

.banner-slide {
  position: relative;
  height: 320px;
  overflow: hidden;
  border-radius: 20px;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.banner-fallback {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #ffe7bd, #fbd2d4);
}

.banner-fallback-title {
  padding: 20px;
  text-align: center;
  font-size: 32px;
  font-weight: 800;
  color: #7c3f00;
}

.banner-overlay {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 24px;
  color: #fff;
  background: linear-gradient(180deg, transparent, rgba(15, 23, 42, 0.8));
}

.banner-label {
  display: inline-block;
  padding: 6px 12px;
  margin-bottom: 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
}

.banner-title {
  font-size: 28px;
  font-weight: 800;
}

.banner-empty {
  height: 180px;
  display: grid;
  place-items: center;
  color: var(--text-regular);
  border: 1px dashed var(--border-soft);
  border-radius: 18px;
}

@media (max-width: 960px) {
  .user-hero {
    display: grid;
    gap: 14px;
  }

  .banner-slide {
    height: 240px;
  }

  .banner-title {
    font-size: 22px;
  }
}
</style>
