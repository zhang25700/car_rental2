<template>
  <div class="page-card">
    <h3 class="section-title">可租车辆</h3>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="品牌 / 型号 / 车牌号" clearable @keyup.enter.native="loadData" />
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </div>

    <div class="vehicle-grid">
      <div v-for="item in tableData" :key="item.id" class="vehicle-card">
        <div class="vehicle-cover">
          <img v-if="item.coverImage && !imageErrors[item.id]" :src="item.coverImage" @error="markImageError(item.id)" />
          <div v-else class="vehicle-placeholder">{{ item.brand }} {{ item.model }}</div>
        </div>
        <div class="vehicle-body">
          <div class="vehicle-title">{{ item.brand }} {{ item.model }}</div>
          <div class="vehicle-meta">车牌号：{{ item.vehicleNo }}</div>
          <div class="vehicle-meta">座位数：{{ item.seatCount }} 座</div>
          <div class="vehicle-meta">状态：{{ statusText(item.status) }}</div>
          <div class="vehicle-desc">{{ item.description || "车辆信息待补充" }}</div>
          <div class="vehicle-foot">
            <div class="vehicle-price">￥{{ item.dailyPrice }} / 天</div>
            <el-button type="primary" :disabled="item.status !== 'AVAILABLE'" @click="openRentDialog(item)">立即租车</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="提交租车订单" :visible.sync="dialogVisible" width="560px">
      <el-form :model="rentForm" label-width="100px">
        <el-form-item label="车辆">
          <el-input :value="selectedVehicleText" disabled />
        </el-form-item>
        <el-form-item label="取车时间">
          <el-date-picker v-model="rentForm.pickupTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="还车时间">
          <el-date-picker v-model="rentForm.returnTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="日租金">
          <el-input :value="rentForm.dailyRent" disabled />
        </el-form-item>
        <el-form-item label="总金额">
          <el-input-number v-model="rentForm.totalAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="rentForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRent">确认租车</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { rentVehicle } from "../api/order";
import { getVehicles } from "../api/vehicle";

const defaultRentForm = () => ({
  vehicleId: null,
  pickupTime: "",
  returnTime: "",
  dailyRent: 0,
  totalAmount: 0,
  remark: ""
});

export default {
  name: "UserVehiclePage",
  data() {
    return {
      query: { pageNum: 1, pageSize: 20, keyword: "", status: "AVAILABLE" },
      tableData: [],
      imageErrors: {},
      dialogVisible: false,
      rentForm: defaultRentForm(),
      selectedVehicle: null
    };
  },
  created() {
    this.loadData();
  },
  computed: {
    selectedVehicleText() {
      return this.selectedVehicle ? `${this.selectedVehicle.brand} ${this.selectedVehicle.model}` : "";
    }
  },
  methods: {
    async loadData() {
      const res = await getVehicles(this.query);
      this.tableData = res.data.list || [];
    },
    resetQuery() {
      this.query = { pageNum: 1, pageSize: 20, keyword: "", status: "AVAILABLE" };
      this.loadData();
    },
    markImageError(id) {
      this.$set(this.imageErrors, id, true);
    },
    statusText(status) {
      return { AVAILABLE: "可租赁", RENTED: "租赁中", MAINTENANCE: "维护中" }[status] || status;
    },
    openRentDialog(item) {
      this.selectedVehicle = item;
      this.rentForm = {
        vehicleId: item.id,
        pickupTime: "",
        returnTime: "",
        dailyRent: item.dailyPrice,
        totalAmount: item.dailyPrice,
        remark: ""
      };
      this.dialogVisible = true;
    },
    async submitRent() {
      await rentVehicle(this.rentForm);
      this.$message.success("租车订单已提交");
      this.dialogVisible = false;
      this.loadData();
      this.$router.push("/user/home");
    }
  }
};
</script>

<style scoped>
.vehicle-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 20px; }
.vehicle-card { border-radius: 20px; overflow: hidden; background: #fff; box-shadow: var(--shadow-soft); border: 1px solid var(--border-soft); }
.vehicle-cover { height: 190px; background: linear-gradient(135deg, #d7efe9, #dbe7f5); }
.vehicle-cover img { width: 100%; height: 100%; object-fit: cover; display: block; }
.vehicle-placeholder { width: 100%; height: 100%; display: grid; place-items: center; padding: 20px; text-align: center; color: var(--text-primary); font-weight: 700; font-size: 22px; }
.vehicle-body { padding: 18px; }
.vehicle-title { font-size: 20px; font-weight: 700; margin-bottom: 10px; }
.vehicle-meta { color: var(--text-regular); margin-bottom: 6px; }
.vehicle-desc { margin: 12px 0 16px; min-height: 40px; color: var(--text-regular); }
.vehicle-foot { display: flex; justify-content: space-between; align-items: center; }
.vehicle-price { font-size: 22px; font-weight: 800; color: var(--brand); }
</style>
