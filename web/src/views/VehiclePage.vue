<template>
  <div class="page-card">
    <h3 class="section-title">车辆管理</h3>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="车牌号 / 品牌 / 型号" clearable @keyup.enter.native="loadData" />
      <el-select v-model="query.status" placeholder="车辆状态" clearable>
        <el-option label="可租赁" value="AVAILABLE" />
        <el-option label="租赁中" value="RENTED" />
        <el-option label="维护中" value="MAINTENANCE" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
      <el-button type="success" @click="openDialog()">新增车辆</el-button>
    </div>

    <el-table :data="tableData">
      <el-table-column prop="vehicleNo" label="车牌号" />
      <el-table-column prop="brand" label="品牌" />
      <el-table-column prop="model" label="型号" />
      <el-table-column prop="dailyPrice" label="日租金" />
      <el-table-column prop="seatCount" label="座位数" />
      <el-table-column label="状态">
        <template slot-scope="{ row }">{{ statusText(row.status) }}</template>
      </el-table-column>
      <el-table-column prop="hotFlag" label="热门">
        <template slot-scope="{ row }">{{ row.hotFlag ? "是" : "否" }}</template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template slot-scope="{ row }">
          <el-button type="text" @click="openDialog(row)">编辑</el-button>
          <el-button type="text" @click="changeStatus(row, 'AVAILABLE')">上架</el-button>
          <el-button type="text" @click="changeStatus(row, 'MAINTENANCE')">维护</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 18px; text-align: right;">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :current-page.sync="query.pageNum"
        :page-size="query.pageSize"
        :total="total"
        @current-change="loadData"
      />
    </div>

    <el-dialog :title="form.id ? '编辑车辆' : '新增车辆'" :visible.sync="dialogVisible" width="640px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="车牌号"><el-input v-model="form.vehicleNo" /></el-form-item>
        <el-form-item label="品牌"><el-input v-model="form.brand" /></el-form-item>
        <el-form-item label="型号"><el-input v-model="form.model" /></el-form-item>
        <el-form-item label="颜色"><el-input v-model="form.color" /></el-form-item>
        <el-form-item label="座位数"><el-input-number v-model="form.seatCount" :min="2" /></el-form-item>
        <el-form-item label="日租金"><el-input-number v-model="form.dailyPrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="可租赁" value="AVAILABLE" />
            <el-option label="租赁中" value="RENTED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="热门车"><el-switch v-model="form.hotFlag" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="封面图"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getVehicles, saveVehicle, updateVehicleStatus } from "../api/vehicle";

const defaultForm = () => ({
  id: null,
  vehicleNo: "",
  brand: "",
  model: "",
  color: "",
  seatCount: 5,
  dailyPrice: 0,
  status: "AVAILABLE",
  hotFlag: 0,
  coverImage: "",
  description: ""
});

export default {
  name: "VehiclePage",
  data() {
    return {
      query: { pageNum: 1, pageSize: 10, keyword: "", status: "" },
      tableData: [],
      total: 0,
      dialogVisible: false,
      form: defaultForm()
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    async loadData() {
      const res = await getVehicles(this.query);
      this.tableData = res.data.list || [];
      this.total = res.data.total || 0;
    },
    resetQuery() {
      this.query = { pageNum: 1, pageSize: 10, keyword: "", status: "" };
      this.loadData();
    },
    openDialog(row) {
      this.form = row ? { ...row } : defaultForm();
      this.dialogVisible = true;
    },
    async submit() {
      await saveVehicle(this.form);
      this.$message.success("保存成功");
      this.dialogVisible = false;
      this.loadData();
    },
    async changeStatus(row, status) {
      await updateVehicleStatus(row.id, status);
      this.$message.success("状态已更新");
      this.loadData();
    },
    statusText(status) {
      return { AVAILABLE: "可租赁", RENTED: "租赁中", MAINTENANCE: "维护中" }[status] || status;
    }
  }
};
</script>
