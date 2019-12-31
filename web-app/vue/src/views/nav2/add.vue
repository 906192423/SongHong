<template>
    <el-form :model="ruleForm" :rules="rules" v-loading="formLoading" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="请选择客户" prop="_Uid">
            <el-select style="width: 300px"
                       v-model="selUser"
                       filterable
                       remote
                       @change="chu()"
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
            <el-button type="primary" :disabled="disabled"  :loading="uloading"  @click="creatUU()">直接创建客户:{{uuuName}}</el-button>
        </el-form-item>
        <el-form-item label="联系电话" style="width:500px" prop="phone">
            <el-input v-model="ruleForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="合计金额" style="width:500px" prop="phone">
            <el-input v-model="ruleForm.amount"></el-input>
        </el-form-item>
        <el-form-item label="交款方式" style="width:500px" prop="earnest">
            <el-radio v-model="ruleForm.earnest" label="1">全款结清</el-radio>
            <el-radio v-model="ruleForm.earnest" label="0">定金付款</el-radio>
            <el-radio v-model="ruleForm.earnest" label="-1">赊账欠款</el-radio>
        </el-form-item>
        <!--<el-form-item label="支付方式" prop="payWay">-->
        <!--<el-select v-model="ruleForm.payWay" placeholder="请选择支付方式" multiple>-->
        <!--<el-option label="微信支付" value="微信支付"></el-option>-->
        <!--<el-option label="支付宝支付" value="支付宝支付"></el-option>-->
        <!--<el-option label="刷卡支付" value="刷卡支付"></el-option>-->
        <!--<el-option label="现金支付" value="现金支付"></el-option>-->
        <!--</el-select>-->
        <!--</el-form-item>-->

        <el-form-item label="交货时间" style="width:500px" prop="leadTime">
            <el-date-picker v-model="ruleForm.leadTime" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
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
                selUser:"",
                user:"",
                ruleForm: {
                    modeTransport : "",//运输方式
                    phone:"",
                    _Uid: "",//客户id
                    leadTime:"",//交货时间
                    remark : "",//备注
                    amount :0,//合计金额
                    addr:"",//交货地址
                    num9:"",//数量
                    earnest:"",
                },
                formLoading:false,
                rules: {
                    _Uid: [
                        { required: true, message: '请输入用户', trigger: 'blur' },
                    ],
                    modeTransport: [
                        { required: true, message: '请输入运输方式', trigger: 'blur' }
                    ],
                    leadTime: [
                        { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                    ],
                    addr: [
                        { required: true, message: '请输入送货地址', trigger: 'blur' }
                    ],
                    earnest: [
                        { required: true, message: '请选择交款方式', trigger: 'change' }
                    ],
                    phone: [
                        { required: true, message: '请选输入联系电话', trigger: 'change' }
                    ],
                },
                users: [],
                list: [],
                loading: false,
                options5: [],
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
                row.amount= parseFloat((row.price*row.num).toFixed(3))
                let total=0.0
                this.goodsTable.forEach(it=>{
                    total=parseFloat(it.amount+total)
                })
                this.ruleForm.amount=total
            },
            change(){
                let goodsTable=[]
                this.value10.forEach(it=>{
                    let g=JSON.parse(it)
                    for(let j = 0,len = this.goodsTable.length; j < len; j++){
                        if(g._id==this.goodsTable[j]._id){
                            g.num=this.goodsTable[j].num
                            g.amount=this.goodsTable[j].amount
                            break
                        }
                    }
                    goodsTable.push(g)
                })
                this.goodsTable=goodsTable
            },
            getGoods(){
                this.gloading=true
                this.VgetJSON('product/getGoods',{name:this.name}).then(data=>{
                    this.options5=[]
                    data.list.forEach(item=>{
                        item.num=0
                        item.amount=0
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
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {


                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
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
                                    total:it.amount,
                                }
                                detail.push(a)
                            })
                            form.detail=JSON.stringify(detail)
                            form.leadTime = (!this.ruleForm.leadTime || this.ruleForm.leadTime == '') ? '' : util.formatDate.format(new Date(this.ruleForm.leadTime), 'yyyy-MM-dd hh:mm:ss');
                            this.VgetJSON('order/creat',form).then(data=>{
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
                    this.VgetJSON('customer/getUsers',{name:query}).then(data=>{
                        if(data.flag){
                            this.users=data.users
                            if(this.users.length>=1){
                                this.disabled=true
                            }else {
                                this.uuuName=query
                                this.disabled=false
                            }
                        }else {
                            this.$message({
                                message: data.remark,
                                type: 'error'
                            });
                        }
                        this.loading=false
                    })
                } else {
                    this.users = [];
                    this.loading=false
                }
            },
            creatUU(){
                if(this.ruleForm._Uid){
                    this.disabled=true
                    return
                }
                this.uloading=true
                let form={
                    name:this.uuuName,
                    phone:this.uuuName,
                    sex:"男",
                }
                this.VgetJSON('customer/creat',form).then(data=>{
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
                    this.uloading=false
                    this.remoteMethod(this.uuuName)
                })
            },
            chu(){
                if(this.selUser){
                    this.user=JSON.parse(this.selUser)
                }else {
                    this.user={}
                }
                this.ruleForm._Uid=this.user._id
                this.ruleForm.phone=this.user.phone
                this.ruleForm.addr=this.user.addr
            }
        },
        mounted() {
            this.getGoods()
        }
    }
</script>