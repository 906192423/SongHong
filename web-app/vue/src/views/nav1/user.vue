<template>
	<el-form ref="form" :model="form" label-width="80px" @submit.prevent="onSubmit" style="margin:20px;width:60%;min-width:600px;" v-loading="loading">
		<el-form-item label="客户名称">
			<el-input v-model="form.name" style="width: 50%;"></el-input>
		</el-form-item>
		<el-form-item label="客户电话">
			<el-input v-model="form.phone" style="width: 50%;"></el-input>
		</el-form-item>
		<el-form-item label="客户邮箱">
			<el-input v-model="form.email" style="width: 50%;"></el-input>
		</el-form-item>
		<el-form-item label="客户qq">
			<el-input v-model="form.qq" style="width: 50%;"></el-input>
		</el-form-item>
		<el-form-item label="客户地址">
			<el-input v-model="form.addr" style="width: 50%;"></el-input>
		</el-form-item>
		<el-form-item label="客户生日">
			<el-col :span="11">
				<el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
			</el-col>
		</el-form-item>
		<el-form-item label="性别">
			<el-select v-model="form.sex" placeholder="请选择性别">
				<el-option label="男" value="1"></el-option>
				<el-option label="女" value="0"></el-option>
			</el-select>
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
				loading:false,
			}
		},
		methods: {
			onSubmit() {
				this.loading=true
				this.form.birth = (!this.form.birth || this.form.birth == '') ? '' : util.formatDate.format(new Date(this.form.birth), 'yyyy-MM-dd');
				$.getJSON('api/customer/creat',this.form).then(data=>{
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
					this.loading=false
				})
			},
			cancel(){
				this.$router.push({ path: '/table' });
			}
		}
	}

</script>