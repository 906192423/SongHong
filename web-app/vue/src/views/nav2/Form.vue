<template>
	<el-form :model="ruleForm" :rules="rules" v-loading="formLoading" ref="ruleForm" label-width="100px" class="demo-ruleForm">
		<el-form-item label="请选择客户" prop="name">
			<el-select style="width: 300px"
					v-model="ruleForm._Uid"
					multiple
					filterable
					remote
					reserve-keyword
					placeholder="请输入姓名或者电话号码来查找"
					:remote-method="remoteMethod"
					:loading="loading">
				<el-option
						v-for="item in users"
						:key="item.value"
						:label="item.label"
						:value="item.value">
				</el-option>
			</el-select>
		</el-form-item>
        <el-form-item label="请选择商品">
            <el-select
                    v-model="value10"
                    multiple
                    filterable
                    default-first-option
                    placeholder="请选择商品">
                <el-option
                        v-for="item in options5"
                        :key="item.value"
                        :label="item.label"
                        :value="item">
                </el-option>
            </el-select>
            <el-input
                    style="width: 150px"
                    placeholder="输入内容查找商品"
                    v-model="name"
                    clearable>
            </el-input>
            <el-button type="primary" v-on:click="getGoods">搜索</el-button>
        </el-form-item>
        <el-form-item label="商品表">
        <el-table
                :data="value10"
                stripe
                style="width:800px">
            <el-table-column
                    prop="value"
                    label="商品名"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="label"
                    label="商品价格"
                    width="180">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="250">
                <template slot-scope="scope">
                    <el-input-number v-model="scope.row.num" :precision="3" :step="0.1" :min="1" :max="1000" @change="addCli(scope.row)"></el-input-number>
                </template>
            </el-table-column>
            <el-table-column
                    prop="amount"
                    label="总额">
            </el-table-column>
        </el-table>
        </el-form-item>
		<el-form-item label="支付方式" prop="payWay">
			<el-select v-model="ruleForm.payWay" placeholder="请选择支付方式">
				<el-option label="微信支付" value="微信支付"></el-option>
				<el-option label="支付宝支付" value="支付宝支付"></el-option>
				<el-option label="刷卡支付" value="刷卡支付"></el-option>
				<el-option label="现金支付" value="现金支付"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="交货时间" required>
			<el-col :span="11">
				<el-form-item prop="leadTime" style="width: 216px">
					<el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.leadTime" style="width: 100%;"></el-date-picker>
				</el-form-item>
			</el-col>
			<!--<el-col class="line" :span="2">-</el-col>-->
			<!--<el-col :span="11">-->
				<!--<el-form-item prop="date2">-->
					<!--<el-time-picker type="fixed-time" placeholder="选择时间" v-model="ruleForm.date2" style="width: 100%;"></el-time-picker>-->
				<!--</el-form-item>-->
			<!--</el-col>-->
		</el-form-item>
		<el-form-item label="运输方式" style="width:500px" prop="modeTransport">
			<el-input v-model="ruleForm.modeTransport"></el-input>
		</el-form-item>
		<el-form-item label="总金额" style="width:500px" prop="amount">
			<el-input v-model="ruleForm.amount"></el-input>
		</el-form-item>
		<el-form-item label="交货地址" prop="addr" style="width:500px">
			<el-input type="textarea" v-model="ruleForm.addr"></el-input>
		</el-form-item>
		<el-form-item label="备注" prop="remark" style="width:500px">
			<el-input type="textarea" v-model="ruleForm.remark"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
			<el-button @click="resetForm('ruleForm')">重置</el-button>
		</el-form-item>
	</el-form>
</template>

<script>
	export default {
		data() {
			return {
                name:"",
				ruleForm: {
					modeTransport : "",//运输方式
					_Uid: "",//客户id
					leadTime: " ",//交货时间
					remark : "",//备注
					amount :0,//合计金额
					payWay:"",//支付方式
					addr:"",//交货地址
				},
				formLoading:false,
				rules: {
					name: [
						{ required: true, message: '请输入活动名称', trigger: 'blur' },
						{ min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
					],
					region: [
						{ required: true, message: '请选择活动区域', trigger: 'change' }
					],
					date1: [
						{ type: 'date', required: true, message: '请选择日期', trigger: 'change' }
					],
					date2: [
						{ type: 'date', required: true, message: '请选择时间', trigger: 'change' }
					],
					type: [
						{ type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
					],
					resource: [
						{ required: true, message: '请选择活动资源', trigger: 'change' }
					],
					desc: [
						{ required: true, message: '请填写活动形式', trigger: 'blur' }
					]
				},
				users: [],
				list: [],
				loading: false,
                options5: [{
                    value: 'HTML',
                    label: 'HTML'
                }, {
                    value: 'CSS',
                    label: 'CSS'
                }, {
                    value: 'JavaScript',
                    label: 'JavaScript'
                }],
                value10: []
			};
		},
		methods: {
            addCli(row){
                console.log("----------------------")
                console.log(row)
            },
            getGoods(){
                $.getJSON('api/product/getGoods',{name:this.name}).then(data=>{
                })
            },
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.loading=true
						$.getJSON('api/order/creat',this.ruleForm).then(data=>{
							if(data.flag){
								this.$notify({
									title: '成功',
									message: data.remark,
									type: 'success'
								});
								this.$refs['ruleForm'].resetFields();
							}else {
								this.$message({
									message: data.remark,
									type: 'error'
								});
							}
							this.loading=false
						})
					} else {
						this.$message({
							message:'请正确填写必要数据！',
							type: 'error'
						});
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			remoteMethod(query) {
				this.loading=true
				if (query !== '') {
					$.getJSON('api/customer/getUsers',{name:query}).then(data=>{
						if(data.flag){
							this.users=data.users
							console.log(data.users)
						}else {
							this.$message({
								message: data.remark,
								type: 'error'
							});
						}
					})
				} else {
					this.users = [];
				}
				this.loading=false
			}
		}
	}
</script>