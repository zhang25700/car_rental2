<template>
  <div class="page-card">
    <h3 class="section-title">客户管理</h3>
    <div class="toolbar">
      <el-button type="success" @click="openDialog()">新增客户</el-button>
      <el-button @click="loadData">刷新</el-button>
    </div>

    <el-table :data="tableData">
      <el-table-column prop="customerName" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="driverLicenseNo" label="驾驶证号" />
      <el-table-column prop="idCardNo" label="身份证号" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="120">
        <template slot-scope="{ row }">
          <el-button type="text" @click="openDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="form.id ? '编辑客户' : '新增客户'" :visible.sync="dialogVisible" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="姓名"><el-input v-model="form.customerName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="驾驶证号"><el-input v-model="form.driverLicenseNo" /></el-form-item>
        <el-form-item label="身份证号"><el-input v-model="form.idCardNo" /></el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCustomers, saveCustomer } from "../api/customer";

const defaultForm = () => ({
  id: null,
  customerName: "",
  phone: "",
  gender: "男",
  driverLicenseNo: "",
  idCardNo: "",
  status: 1
});

export default {
  name: "CustomerPage",
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      form: defaultForm()
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    async loadData() {
      const res = await getCustomers();
      this.tableData = res.data || [];
    },
    openDialog(row) {
      this.form = row ? { ...row } : defaultForm();
      this.dialogVisible = true;
    },
    async submit() {
      await saveCustomer(this.form);
      this.$message.success("保存成功");
      this.dialogVisible = false;
      this.loadData();
    }
  }
};
</script>
