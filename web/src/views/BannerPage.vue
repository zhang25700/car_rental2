<template>
  <div class="page-card">
    <h3 class="section-title">广告位管理</h3>
    <div class="toolbar">
      <el-button type="success" @click="openDialog()">新增广告</el-button>
      <el-button @click="loadData">刷新</el-button>
    </div>

    <el-table :data="tableData">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="imageUrl" label="图片地址" />
      <el-table-column prop="redirectUrl" label="跳转地址" />
      <el-table-column prop="sort" label="排序" width="90" />
      <el-table-column prop="status" label="状态" width="90" />
      <el-table-column label="操作" width="120">
        <template slot-scope="{ row }">
          <el-button type="text" @click="openDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="form.id ? '编辑广告' : '新增广告'" :visible.sync="dialogVisible" width="580px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片地址"><el-input v-model="form.imageUrl" /></el-form-item>
        <el-form-item label="跳转地址"><el-input v-model="form.redirectUrl" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
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
import { getActiveBanners, saveBanner } from "../api/banner";

const defaultForm = () => ({
  id: null,
  title: "",
  imageUrl: "",
  redirectUrl: "",
  sort: 0,
  status: 1
});

export default {
  name: "BannerPage",
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
      const res = await getActiveBanners();
      this.tableData = res.data || [];
    },
    openDialog(row) {
      this.form = row ? { ...row } : defaultForm();
      this.dialogVisible = true;
    },
    async submit() {
      await saveBanner(this.form);
      this.$message.success("保存成功");
      this.dialogVisible = false;
      this.loadData();
    }
  }
};
</script>
