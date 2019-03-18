<template>
	<el-form :model="ruleForm" :rules="rules" v-loading="formLoading" ref="ruleForm" label-width="100px" class="demo-ruleForm">
		<el-form-item label="请选择客户" prop="_Uid">
			<el-select style="width: 300px"
					v-model="ruleForm._Uid"
					filterable
					remote
					reserve-keyword
					placeholder="请输入姓名或者电话号码来查找"
					:remote-method="remoteMethod"
                       clearable
					:loading="loading">
				<el-option
						v-for="item in users"
						:key="item.value"
						:label="item.label"
						:value="item.value">
				</el-option>
			</el-select>
            <el-button type="primary" :disabled="disabled"  :loading="uloading" v-on:click="creatU()">创建客户</el-button>
		</el-form-item>
        <el-form-item label="请选择商品">
            <el-select
                    v-model="value10"
                    multiple
                    filterable
                    :loading="gloading"
                    @change="change()"
                    placeholder="请选择商品">
                <el-option
                        v-for="item in options5"
                        :key="item.label"
                        :label="item.label"
                        :value="item.value"
                        :disabled="item.disabled">
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
        <el-form-item label="商品列表">
		<el-table
				:data="goodsTable"
				border
				height="200"
				:summary-method="getSummaries"
				show-summary
				style="width: 100%; margin-top: 20px">
			<el-table-column
					prop="cold"
					label="商品编号"
					width="180">
			</el-table-column>
			<el-table-column
					prop="name"
					label="产品名称">
			</el-table-column>
			<el-table-column
					prop="price"
					label="商品单价">
			</el-table-column>
			<el-table-column label="数量" width="250">
				<template slot-scope="scope">
                    <el-input
                            placeholder="请输入内容"
                            v-model="scope.row.num"
                            @change="addCli(scope.row)"
                            clearable>
                    </el-input>
				</template>
			</el-table-column>
			<el-table-column fixed="right"   prop="amount" label="小计">
			</el-table-column>
		</el-table>
		</el-form-item>
		<el-form-item label="交货时间" required>
			<el-col :span="11">
				<el-form-item prop="leadTime" style="width: 216px">
					<el-date-picker v-model="ruleForm.leadTime" type="date" placeholder="选择日期"></el-date-picker>
				</el-form-item>
			</el-col>
		</el-form-item>
		<el-form-item label="运输方式" style="width:500px" prop="modeTransport">
			<el-input v-model="ruleForm.modeTransport"></el-input>
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
	import util from '../../common/js/util'
	export default {
		data() {
			return {
                name:"",
                uuuName:"",
                disabled:true,
                gloading:false,
                uloading:false,
				ruleForm: {
					modeTransport : "",//运输方式
					_Uid: "",//客户id
					leadTime: " ",//交货时间
					remark : "",//备注
					amount :0,//合计金额
					payWay:"",//支付方式
					addr:"",//交货地址
					num9:"",//数量
                    earnest:0,
				},
				formLoading:false,
				rules: {
                    _Uid: [
						{ required: true, message: '请输入用户', trigger: 'blur' },
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
                value10: [],
                goodsTable:[],
			};
		},
		methods: {
			getSummaries(param) {
				const { columns, data } = param;
				const sums = [];
				columns.forEach((column, index) => {
					if (index === 0) {
						sums[index] = '合计金额';
						return;
					}
					if (index === 1||index === 2||index === 3) {
						sums[index] = '  ';
						return;
					}

					const values = data.map(item => Number(item[column.property]));
					if (!values.every(value => isNaN(value))) {
						sums[index] = values.reduce((prev, curr) => {
							const value = Number(curr);
							if (!isNaN(value)) {
								return prev + curr;
							} else {
								return prev;
							}
						}, 0);
						sums[index]=sums[index].toFixed(3)+' 元';
					} else {
						sums[index] = 'N/A';
					}
				});

				return sums;
			},
            addCli(row){
                console.log("num:"+row.num+"price: "+row.price)
                row.amount= parseFloat((row.price*row.num).toFixed(3))
                console.log((row.price*row.num).toFixed(3))
                let total=0.0
                this.goodsTable.forEach(it=>{
                    total=parseFloat(it.amount+total)
                })
                this.ruleForm.amount=total
            },
            change(){
                this.goodsTable=[]
                this.value10.forEach(it=>{
                    let g=JSON.parse(it)
                    g.amount=0
                    this.goodsTable.push(g)
                })
            },
            getGoods(){
                this.gloading=true
                $.getJSON('api/product/getGoods',{name:this.name}).then(data=>{
                    this.options5=[]
                    data.list.forEach(item=>{
                        let disabled
                        if(item.state==0)
                            disabled=false
                        else
                            disabled=true
                        this.options5.push({value:JSON.stringify(item),label:item.name,disabled:disabled})
                    })
                    this.gloading=false
                })
            },
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.loading=true
                        let form=JSON.parse(JSON.stringify(this.ruleForm))
                        let detail=[]
                        this.goodsTable.forEach(it=>{
                            let a={
                                _id:it._id,
                                name:it.name,
                                code:it.code,
                                num:it.num,
                                price:it.price,
                            }
                            detail.push(a)
                        })
                        form.detail=JSON.stringify(detail)
						form.leadTime = (!this.ruleForm.leadTime || this.ruleForm.leadTime == '') ? '' : util.formatDate.format(new Date(this.ruleForm.leadTime), 'yyyy-MM-dd');
						$.getJSON('api/order/creat',form).then(data=>{
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
						}else {
							this.$message({
								message: data.remark,
								type: 'error'
							});
						}
                        if(this.users.length>=1){
                            this.disabled=true
                        }else {
                            this.uuuName=query
                            this.disabled=false
                        }
					})
				} else {
					this.users = [];
					this.disabled=true
				}
				this.loading=false
			},
            creatU(){
			    this.uloading=true
			    let form={
                    name:this.uuuName,
                    phone:this.uuuName,
                    sex:"男",
                }
                $.getJSON('api/customer/creat',form).then(data=>{
                    if(data.flag){
                        this.$notify({
                            title: '创建用户成功',
                            message: data.remark,
                            type: 'success'
                        });
                        this.disabled=true
                    }else {
                        this.$message({
                            message: data.remark,
                            type: 'error'
                        });
                    }
                })
                this.uloading=false
                this.remoteMethod(this.uuuName)
            }
		},
        mounted() {
            this.getGoods()
        }
	}
</script>