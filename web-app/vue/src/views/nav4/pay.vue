<template>
    <el-form :model="dynamicValidateForm" :rules="rules" ref="dynamicValidateForm" style="width: 100%"  :v-loading="loading" label-width="100px">
        <el-form-item label="请选择订单：">
            <el-autocomplete
                    v-model="state4"
                    clearable
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入订单号"
                    @select="handleSelect"
            ></el-autocomplete>
        </el-form-item>
        <el-alert
                :title="info"
                type="info"
                center
                :closable="false"
                show-icon>
        </el-alert>
        <el-table :data="order" highlight-current-row  style="width: 100%;"ref="multipleTable">
            <el-table-column type="expand">
                <template slot-scope="scope">
                    <div>
                        <el-table
                                :data="scope.row.detail"
                                style="width: 100%">
                            <el-table-column
                                    prop="name"
                                    label="商品名"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="code"
                                    label="商品码"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="price"
                                    width="120"
                                    label="价格">
                            </el-table-column>
                            <el-table-column
                                    prop="num"
                                    width="120"
                                    label="数量">
                            </el-table-column>
                            <el-table-column
                                    prop="total"
                                    label="总价">
                            </el-table-column>
                        </el-table>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="sellCode" label="订单号" width="120">
            </el-table-column>
            <el-table-column prop="userName" label="消费者" width="120">
            </el-table-column>
            <el-table-column prop="amount" label="总金额" width="120">
            </el-table-column>
            <el-table-column prop="leadTime" label="交货时间" width="220">
            </el-table-column>
            <el-table-column label="交货地址" min-width="180">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>{{ scope.row.addr}}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="info" color="blue">查看交货地址</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="phone" label="联系电话" width="220">
            </el-table-column>
            <el-table-column label="交款方式" width="150">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.earnest==0" type="warning">定金</el-tag>
                    <el-tag v-if="scope.row.earnest==1" type="success">全款</el-tag>
                    <el-tag v-if="scope.row.earnest==-1">欠款</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="生产状态" width="150">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.state==0">待处理</el-tag>
                    <el-tag v-if="scope.row.state==1" type="warning">生产中</el-tag>
                    <el-tag v-if="scope.row.state==2" type="success">生产完成</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="creatName" label="销售员" width="120">
            </el-table-column>
        </el-table>
        <el-alert
                :title="info"
                type="info"
                center
                :closable="false"
                show-icon>
        </el-alert>
        <el-alert
                v-if="needCash>0"
                title="需要交的金额"
                type="warning"
                :closable="false"
                center
                :description="needCash+'元'"
                show-icon>
        </el-alert>
        <el-form-item v-if="this.order[0]&&this.order[0].payState!=1" label="优惠金额：" style="width: 200px">
            <el-input v-model="dynamicValidateForm.cutAmount" @change="cha"></el-input>
        </el-form-item>
        <el-form-item
                v-for="(domain, index) in dynamicValidateForm.domains"
                :label="'支付方式：'"
                :key="domain.key"
        >
            <div style="margin-top: 15px;">
                <el-input placeholder="请输入内容" v-model="domain.amount"class="input-with-select" style="width: 400px">
                    <el-select v-model="domain.name" slot="prepend" placeholder="请选择">
                        <el-option
                                v-for="item in options2"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                                :disabled="item.disabled">
                        </el-option>
                    </el-select>
                </el-input>
                <el-button v-if="index!=0" @click.prevent="removeDomain(domain)">删除</el-button>
            </div>
        </el-form-item>
        <el-form-item label="备注：" style="width:500px">
            <el-input type="textarea" v-model="dynamicValidateForm.remark"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>
            <el-button @click="addDomain">新增支付方式</el-button>
            <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        data() {
            return {
                loading:false,
                state4:'',
                info:"",
                order:[],
                needCash:0,//需要的金额
                options2: [{
                    value: 1,
                    label: '支付宝',
                    disabled: false
                }, {
                    value: 2,
                    label: '微信支付',
                    disabled: false
                }, {
                    value: 3,
                    label: '现金支付',
                    disabled: false
                }, {
                    value: 4,
                    label: '刷卡支付',
                    disabled: false
                }],
                rules:{
                    order: [
                        { required: true, message: '请选择订单号', trigger: 'blur' },
                    ],
                    type:[
                        { required: true, message: '请选择支付方式', trigger: 'change' }
                    ],
                    amount:[
                        { required: true, message: '金额不能为空'},
                        { type: 'number', message: '金额必须为数字值'}
                    ]
                },
                dynamicValidateForm: {
                    _orderId:"",
                    earnest:"",
                    cutAmount:0,
                    domains: [{
                        name:'',
                        amount:'',
                    }],
                    remark:''
                }
            };
        },
        methods: {
            cha(){
                if(!this.isNumber(this.dynamicValidateForm.cutAmount)){
                    this.$message({
                        message:"请输入数字",
                        type: 'error'
                    });
                    this.dynamicValidateForm.cutAmount=0
                    return
                }
            },
            clear(){
                this.needCash=0
                this.order=[]
            },
            querySearchAsync(queryString, cb) {
                if(this.state4==""){
                    this.clear()
                }
                this.VgetJSON("order/cheOrder",{code:queryString}).then(data=>{
                    cb(data.list)
                })
            },
            handleSelect(item) {
                this.dynamicValidateForm._orderId=item.item._id
                this.order=[item.item]
                this.dynamicValidateForm.earnest=this.order[0].payState
                if(this.order[0].payState==1){
                    this.info="当前交款方式：付定金"
                }else if(this.order[0].payState==2){
                    this.info="当前交款方式：支付剩余金额"
                    this.countCash(this.order[0].cashList)
                }else if(this.order[0].payState==0){
                    this.info="当前交款方式：一次性结清"
                    this.needCash=this.order[0].amount
                }else {
                    this.info="当前交款方式：额外交款"
                }
            },
            countCash(list){
                if(!list[0]){
                    list[0]={
                        amount:0
                    }
                }
                this.needCash=this.order[0].amount-list[0].amount
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.dynamicValidateForm._orderId==""){
                            this.$message({
                                message:"请选择订单",
                                type: 'error'
                            });
                            return
                        }
                        if(this.dynamicValidateForm.domains.length<=0){
                            this.$message({
                                message:"请至少选择一种支付方式",
                                type: 'error'
                            });
                            return
                        }
                        let amount=0
                        for(let i=0;i<this.dynamicValidateForm.domains.length;i++){
                            if(!this.dynamicValidateForm.domains[i].name||this.dynamicValidateForm.domains[i].name==""){
                                this.$message({
                                    message:"请选择支付方式",
                                    type: 'error'
                                });
                                return
                            }
                            if(!this.isNumber(this.dynamicValidateForm.domains[i].amount)){
                                this.$message({
                                    message:"请输入金额",
                                    type: 'error'
                                });
                                return
                            }
                            amount+=parseFloat(this.dynamicValidateForm.domains[i].amount)
                        }
                        if(this.order[0].payState!=-1){
                            let cc=amount-this.needCash+parseFloat(this.dynamicValidateForm.cutAmount)
                            if(cc<0){
                                this.$message({
                                    message:"付款金额不足"+-cc,
                                    type: 'error'
                                });
                                return
                            }
                            if(this.order[0].payState==1){
                                cc=amount-this.order[0].amount+parseFloat(this.dynamicValidateForm.cutAmount)
                            }
                            if(cc>0){
                                this.$message({
                                    message:"付款金额超出"+cc,
                                    type: 'error'
                                });
                                return
                            }
                        }
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.loading=true
                            let form=JSON.parse(JSON.stringify(this.dynamicValidateForm))
                            form.domains=JSON.stringify(form.domains)
                            this.VgetJSON('cash/creat',form).then(data=>{
                                if(data.flag){
                                    this.$notify({
                                        title: '成功',
                                        message: data.remark,
                                        type: 'success'
                                    });
                                    this.dynamicValidateForm._orderId=""
                                    this.dynamicValidateForm.remark=""
                                    this.order=[]
                                    this.state4=""
                                    this.dynamicValidateForm.domains=[{
                                        name:'',
                                        amount:'',
                                    }]
                                }else {
                                    this.$message({
                                        message: data.remark,
                                        type: 'error'
                                    });
                                }
                                this.loading=false
                            })
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            isNumber( s ) {
                let regu = "^[0-9]+\.?[0-9]*$";
                let re = new RegExp(regu);
                if (re.test(s))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            removeDomain(item) {
                var index = this.dynamicValidateForm.domains.indexOf(item)
                if (index !== -1) {
                    this.dynamicValidateForm.domains.splice(index, 1)
                }
                this.cha()
            },
            addDomain() {
                this.dynamicValidateForm.domains.push({
                    key: Date.now()
                });
            }
        }
    }
</script>

<style scoped>
    .el-select{
        width: 130px;
    }
</style>