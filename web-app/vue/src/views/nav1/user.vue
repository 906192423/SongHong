<template>
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
</template>

<script>
	import util from '../../common/js/util'
	export default {
		data() {
			return {
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
		methods: {
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
								this.$refs['form'].resetFields();
								this.loading=false
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
			cancel(){
				this.$router.push({ path: '/table' });
			}
		}
	}

</script>