<template>
    <el-form>
        <el-form-item label="请选择订单：">
            <el-autocomplete
                    v-model="state4"
                    clearable
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入订单号"
                    @select="handleSelect"
            ></el-autocomplete>
        </el-form-item>
    <div v-if="order[0]">
    <div class="page" id="printMe" style="background:#fff;">
        <center><h1><b> 辽宁洪锋工贸有限责任公司--销售单</b></h1></center>
        <h3>
        <b><p>  销售员电话：{{ph}}</p>
        <el-table :data="order" :header-cell-style="{color:'#000000' }" cell-style="font-weight: 700;" style="color:#000000":cell-style="{padding:'1px'}">
            <el-table-column prop="sellCode" label="订单号" width="180":cell-style="{padding:'1px'}">
            </el-table-column>
            <el-table-column prop="userName" label="客户姓名" width="180":cell-style="{padding:'1px'}">
            </el-table-column>
            <el-table-column prop="amount" label="总金额" width="180" :cell-style="{padding:'1px'}">
            </el-table-column>
            <el-table-column prop="leadTime" label="交货时间":cell-style="{padding:'1px'}" >
            </el-table-column>
        </el-table>
        <el-table :data="order" :header-cell-style="{color:'#000000' }" cell-style="font-weight: 700;" style="color:#000000":cell-style="{padding:'1px'}">
            <el-table-column label="交货地址"prop="addr" width="180":cell-style="{padding:'1px'}">
            </el-table-column>
            <el-table-column prop="phone" label="联系电话" width="180":cell-style="{padding:'1px'}">
            </el-table-column>
            <el-table-column label="交款方式" width="180":cell-style="{padding:'1px'}">
                <template slot-scope="scope">
                    <span v-if="scope.row.earnest==0">定金</span>
                    <span v-if="scope.row.earnest==1" >全款</span>
                    <span v-if="scope.row.earnest==-1">欠款</span>
                </template>
            </el-table-column>
            <el-table-column prop="creatName" label="销售员" >
            </el-table-column>
        </el-table></b>
        <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
        <br><br><br><br><br><br><br><br><br><br><br>
        <h4 v-if="order[0]">备注：{{order[0].remark}}</h4>
        <template v-for="(item,index) in this.cashList">
            <strong>
        <el-table :data="[item]" style="color:#000000" cell-style="font-weight: 700;" :header-cell-style="{color:'#000000' }" :cell-style="{padding:'1px'}">
            <el-table-column prop="code" label="付款单号" width="180">
            </el-table-column>
            <el-table-column prop="cutAmount" label="优惠金额" width="180">
            </el-table-column>
            <el-table-column prop="amount" label="总金额" width="180" >
            </el-table-column>
            <el-table-column label="支付方式" width="180">
                <template slot-scope="scope">
                    <span v-if="scope.row.earnest==0">全款一次结清</span>
                    <span v-if="scope.row.earnest==1">付定金</span>
                    <span v-if="scope.row.earnest==2">支付剩余金额</span>
                </template>
            </el-table-column>
        </el-table>
            </strong>
        </template>
        <p >公司地址：辽宁省铁岭市调兵山市红房一条街北金山新城南门 </p>
        <p >订购电话/钢结构，彩钢板，白钢板销售部： 13804103658 /五金，电缆，钢材批发部： 15941038386 </p></h3>
    </div>
    <el-button v-print="'#printMe'" @click="prin()">打印页面</el-button>
    </div>

    </el-form>
</template>

<script>
    export default {
        data() {
            return {
                loading:false,
                state4:'',
                info:"",
                ph:"",
                cashList:[],
                order:[
                    {detail:[],
                    },
                ],
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
            prin(){
                this.VgetJSON('Order/print',{id:this.order[0]._id}).then(data=>{
                    this.order=[
                        {detail:[],
                        },
                    ]
                    this.clear()
                    this.dynamicValidateForm={
                        _orderId:"",
                            earnest:"",
                            cutAmount:0,
                            domains: [{
                            name:'',
                            amount:'',
                        }],
                            remark:''
                    }
                    this.cashList=[]
                })
            },
            lookCash(list){
                this.dialog=true
                this.VgetJSON('cash/getInfoList',{list:JSON.stringify(list)}).then(data=>{
                    this.cashList=data.list
                })
            },
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
                this.VgetJSON("order/printOrder",{code:queryString}).then(data=>{
                    this.ph=data.ph
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
                this.lookCash(item.item.cashList)
            },
            countCash(list){
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
                this.chan()
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
    .div-a{ float:left;width:49%;border:1px solid #000}
    .div-b{ float:left;width:49%;border:1px solid #000}
    .page {
        width: 21cm;
        min-height: 29.7cm;
        padding: 0cm;
        margin: 1cm auto;
        border: 1px #D3D3D3 solid;
        border-radius: 5px;
        background: white;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        position: relative;
    }

    @page {
        size: A4;
        margin: 0;
    }
    @media print {
        .page {
            margin: 0;
            border: initial;
            border-radius: initial;
            width: initial;
            min-height: initial;
            box-shadow: initial;
            background: initial;
            page-break-after: always;
        }
    }
    table{
        border-collapse: collapse; /*去掉双重的border*/
        width: 21cm;
        height: 29.7cm;
        font-size: 14px;
        text-align: center;
    }
    table tr:nth-child(1) td{
        width: 25%;
    }
    .el-table th {
         padding: 0px;
        min-width: 0;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        text-overflow: ellipsis;
        vertical-align: middle;
        position: relative;
        text-align: left;
    }
</style>