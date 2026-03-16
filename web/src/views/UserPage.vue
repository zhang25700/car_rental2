<template>
  <div class="page-card">
    <h3 class="section-title">用户管理</h3>
    <div class="toolbar">
      <el-button type="success" @click="openDialog()">新增账号</el-button>
      <el-button @click="loadData">刷新</el-button>
    </div>

    <el-table :data="tableData">
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="role" label="角色" />
      <el-table-column label="状态">
        <template slot-scope="{ row }">{{ row.status === 1 ? "启用" : "停用" }}</template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" width="180" />
      <el-table-column label="操作" width="120">
        <template slot-scope="{ row }">
          <el-button type="text" @click="openDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="form.id ? '编辑账号' : '新增账号'" :visible.sync="dialogVisible" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="运营" value="OPERATOR" />
            <el-option label="客服" value="STAFF" />
            <el-option label="用户端账号" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item :label="form.id ? '重置密码' : '登录密码'">
          <el-input v-model="form.password" placeholder="留空则使用默认密码 password" show-password />
        </el-form-item>
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
import { getUsers, saveUser } from "../api/user";

const defaultForm = () => ({
  id: null,
  username: "",
  realName: "",
  role: "STAFF",
  password: "",
  status: 1
});

export default {
  name: "UserPage",
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
      const res = await getUsers();
      this.tableData = res.data || [];
    },
    openDialog(row) {
      this.form = row
        ? {
            id: row.id,
            username: row.username,
            realName: row.realName,
            role: row.role,
            password: "",
            status: row.status
          }
        : defaultForm();
      this.dialogVisible = true;
    },
    async submit() {
      await saveUser(this.form);
      this.$message.success("保存成功");
      this.dialogVisible = false;
      this.loadData();
    }
  }
};
</script>
