<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="输入姓名或者电话来查找"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getUsers">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="users" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="name" label="姓名" width="120" sortable>
			</el-table-column>
			<el-table-column prop="phone" label="电话" width="140" sortable>
			</el-table-column>
			<el-table-column prop="sex" label="性别" width="100" :formatter="formatSex" sortable>
			</el-table-column>
			<el-table-column prop="birth" label="生日" width="120" sortable>
			</el-table-column>
			<el-table-column prop="addr" label="地址" min-width="180" sortable>
			</el-table-column>
			<el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="姓名" prop="name">
					<el-input v-model="editForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="性别">
					<el-radio-group v-model="editForm.sex">
						<el-radio class="radio" :label="1">男</el-radio>
						<el-radio class="radio" :label="0">女</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
					<el-input v-model="editForm.email" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="qq" prop="qq">
					<el-input v-model="editForm.qq" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="电话" prop="phone">
					<el-input v-model="editForm.phone" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="生日">
					<el-date-picker type="date" placeholder="选择日期" v-model="editForm.birth"></el-date-picker>
				</el-form-item>
				<el-form-item label="身份证号" prop="idCardNumber">
					<el-input v-model="editForm.idCardNumber" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="地址">
					<el-input type="textarea" v-model="editForm.addr"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>


	<el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false" v-loading="addLoading">
		<el-form ref="form" :model="form" :rules="rules" label-width="80px" @submit.prevent="onSubmit" style="margin:20px;width:60%;min-width:600px;" v-loading="loading">
			<el-form-item label="客户名称" prop="name">
				<el-input v-model="form.name" style="width: 50%;"></el-input>
			</el-form-item>
			<el-form-item label="客户电话" prop="phone">
				<el-input v-model="form.phone" style="width: 50%;"></el-input>
			</el-form-item>
			<el-form-item label="客户邮箱">
				<el-input v-model="form.email" style="width: 50%;"></el-input>
			</el-form-item>
			<el-form-item label="客户qq">
				<el-input v-model="form.qq" style="width: 50%;"></el-input>
			</el-form-item>
			<el-form-item label="客户生日">
				<el-col :span="11">
					<el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
				</el-col>
			</el-form-item>
			<el-form-item label="性别" prop="sex">
				<el-select v-model="form.sex" placeholder="请选择性别">
					<el-option label="男" value="1"></el-option>
					<el-option label="女" value="0"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="地址">
				<el-input type="textarea" v-model="form.addr"></el-input>
			</el-form-item>
			<el-form-item label="备注">
				<el-input type="textarea" v-model="form.remark"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="onSubmit">立即创建</el-button>
				<el-button @click="cancel">取消</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	</section>
</template>

<script>
	import util from '../../common/js/util'
	export default {
		data() {
			return {
				filters: {
					name: ''
				},
				users: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					name: [
						{ required: true, message: '请输入姓名', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					id:"",
					name:"",
					sex:"",
					age:"",
					birth: '',
					addr: ''
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					name: [
						{ required: true, message: '请输入姓名', trigger: 'blur' }
					]
				},
				//新增界面数据
				form: {
					name: '',
					email : "",
					idCardNumber : "",
					phone : "",
					qq : " ",
					remark : "",
					sex:"",
					birth:"",
					addr:"",
				},
				rules:{
					name: [
						{ required: true, message: '请输入姓名', trigger: 'blur' },
						{ min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
					],
					email : "",
					idCardNumber : "",
					phone : [
						{ required: true, message: '请输入电话', trigger: 'blur' },
						{ min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
					],
					qq : " ",
					remark : "",
					sex:[
						{ required: true, message: '请选择性别', trigger: 'change' }
					],
					birth:"",
					addr:"",
				},
				loading:false,

			}
		},
		created: function () {
			//页面加载时取到客户表
			// this.VgetJSON('/api/customer/getCustomers',{page:1},d=>{
			// 	console.log("取到用户数据")
			// 	console.log(d)
			// 	this.users=d
			// })
		},
		methods: {
			cancel(){
				this.addFormVisible=false
			},
			onSubmit() {
				this.$refs['form'].validate((valid) => {
					if (valid) {
						this.loading=true
						this.form.birth = (!this.form.birth || this.form.birth == '') ? '' : util.formatDate.format(new Date(this.form.birth), 'yyyy-MM-dd');
						this.VgetJSON('customer/creat',this.form).then(data=>{
							if(data.flag){
								this.$notify({
									title: '成功',
									message: data.remark,
									type: 'success'
								});
								this.form= {
									name: '',
									email : "",
									idCardNumber : "",
									phone : "",
									qq : " ",
									remark : "",
									sex:"",
									birth:"",
									addr:"",
								}
								this.loading=false
								this.addFormVisible=false
							}else {
								this.$message({
									message: data.remark,
									type: 'error'
								});
							}
						})
					} else {
						this.$message({
							message:'请填写必要数据！',
							type: 'error'
						});
						return false;
					}
				});
			},
			//性别显示转换
			formatSex: function (row) {
				return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知';
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getUsers();
			},
			//获取用户列表
			getUsers() {
				let para = {
					page: this.page,
					name: this.filters.name
				};
				this.listLoading = true;
				this.VgetJSON('customer/getCustomers',para).then(d=>{
					console.log("取到用户数据")
					console.log(d)
					this.users=d.users
					this.total=d.num
					this.listLoading = false;
				})
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { _id: row._id };
					this.VgetJSON('customer/delete',para).then(data=>{
						if(data.flag){
							this.$message({
								message:data.remark,
								type: 'success'
							});
						}else {
							this.$message({
								message: data.remark,
								type: 'error'
							});
						}
						this.listLoading = false
						this.getUsers();
					}).catch(() => {
						this.$message({
							message: "请求超时",
							type: 'error'
						});
					});
				}).catch(() => {

				});
			},
			//显示编辑界面
			handleEdit: function (row) {
			    console.log("111111111111111111")
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
			},
			//编辑
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.editLoading = true;
							let para = Object.assign({}, this.editForm);
							para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
							this.VgetJSON('customer/edit',para).then(data=>{
								this.editLoading = false;
								if(data.flag){
									this.$message({
										message:data.remark,
										type: 'success'
									});
								}else {
									this.$message({
										message: data.remark,
										type: 'error'
									});
								}
								this.$refs['editForm'].resetFields();
								this.editFormVisible = false;
								this.getUsers();
							});
						});
					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
				var ids=[]
				ids = this.sels.map(item => item._id);
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { ids:ids };
					this.VgetJSON('customer/deletes',para).then(data=>{
						this.listLoading = false;
						if(data.flag){
							this.$message({
								message:data.remark,
								type: 'success'
							});
						}else {
							this.$message({
								message: data.remark,
								type: 'error'
							});
						}
						this.getUsers();
					})
				}).catch(() => {

				});
			}
		},
		mounted() {
			this.getUsers();
		}
	}

</script>

<style scoped>

</style>