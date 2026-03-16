<template>
  <div class="page-card">
    <h3 class="section-title">订单管理</h3>
    <div class="toolbar">
      <el-input v-model="query.orderNo" placeholder="订单号" clearable />
      <el-select v-model="query.orderStatus" placeholder="订单状态" clearable>
        <el-option label="CREATED" value="CREATED" />
        <el-option label="ONGOING" value="ONGOING" />
        <el-option label="FINISHED" value="FINISHED" />
        <el-option label="CANCELLED" value="CANCELLED" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
      <el-button type="success" @click="openDialog">新增订单</el-button>
    </div>

    <el-table :data="tableData">
      <el-table-column prop="orderNo" label="订单号" width="220" />
      <el-table-column prop="customerId" label="客户ID" />
      <el-table-column prop="vehicleId" label="车辆ID" />
      <el-table-column prop="pickupTime" label="取车时间" width="180" />
      <el-table-column prop="returnTime" label="还车时间" width="180" />
      <el-table-column prop="totalAmount" label="总金额" />
      <el-table-column prop="orderStatus" label="状态" />
      <el-table-column label="操作" width="180">
        <template slot-scope="{ row }">
          <el-button type="text" @click="changeStatus(row, 'ONGOING')">进行中</el-button>
          <el-button type="text" @click="changeStatus(row, 'FINISHED')">完成</el-button>
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

    <el-dialog title="新增订单" :visible.sync="dialogVisible" width="680px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="客户ID"><el-input-number v-model="form.customerId" :min="1" /></el-form-item>
        <el-form-item label="车辆ID"><el-input-number v-model="form.vehicleId" :min="1" /></el-form-item>
        <el-form-item label="取车时间">
          <el-date-picker v-model="form.pickupTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="还车时间">
          <el-date-picker v-model="form.returnTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="日租金"><el-input-number v-model="form.dailyRent" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="总金额"><el-input-number v-model="form.totalAmount" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">创建</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { createOrder, getOrders, updateOrderStatus } from "../api/order";

const defaultForm = () => ({
  customerId: 1,
  vehicleId: 1,
  pickupTime: "",
  returnTime: "",
  dailyRent: 0,
  totalAmount: 0,
  remark: "",
  orderStatus: "CREATED"
});

export default {
  name: "OrderPage",
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        orderNo: "",
        orderStatus: ""
      },
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
      const res = await getOrders(this.query);
      this.tableData = res.data.list || [];
      this.total = res.data.total || 0;
    },
    resetQuery() {
      this.query = { pageNum: 1, pageSize: 10, orderNo: "", orderStatus: "" };
      this.loadData();
    },
    openDialog() {
      this.form = defaultForm();
      this.dialogVisible = true;
    },
    async submit() {
      await createOrder(this.form);
      this.$message.success("订单已创建");
      this.dialogVisible = false;
      this.loadData();
    },
    async changeStatus(row, status) {
      await updateOrderStatus(row.id, status);
      this.$message.success("订单状态已更新");
      this.loadData();
    }
  }
};
</script>
