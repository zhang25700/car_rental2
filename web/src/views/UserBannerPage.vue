<template>
  <div class="page-card">
    <h3 class="section-title">优惠活动</h3>
    <el-row :gutter="20">
      <el-col v-for="item in banners" :key="item.id" :xs="24" :sm="12">
        <div class="banner-card">
          <div class="banner-image">
            <img v-if="item.imageUrl && !imageErrors[item.id]" :src="item.imageUrl" @error="markImageError(item.id)" />
            <div v-else class="banner-placeholder">{{ item.title }}</div>
          </div>
          <div class="banner-title">{{ item.title }}</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getActiveBanners } from "../api/banner";

export default {
  name: "UserBannerPage",
  data() {
    return {
      banners: [],
      imageErrors: {}
    };
  },
  async created() {
    const res = await getActiveBanners();
    this.banners = res.data || [];
  },
  methods: {
    markImageError(id) {
      this.$set(this.imageErrors, id, true);
    }
  }
};
</script>

<style scoped>
.banner-card { margin-bottom: 20px; padding: 20px; border-radius: 18px; background: linear-gradient(180deg, #fff, #f8fbfd); border: 1px solid var(--border-soft); box-shadow: var(--shadow-soft); }
.banner-image { height: 180px; margin-bottom: 16px; border-radius: 14px; overflow: hidden; background: linear-gradient(135deg, #ffe7bd, #fbd2d4); }
.banner-image img { width: 100%; height: 100%; object-fit: cover; display: block; }
.banner-placeholder { width: 100%; height: 100%; display: grid; place-items: center; padding: 20px; text-align: center; font-size: 24px; font-weight: 700; color: #7c3f00; }
.banner-title { font-size: 18px; font-weight: 700; }
</style>
