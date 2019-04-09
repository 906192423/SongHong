<template>
    <el-form :model="ruleForm" :rules="rules" v-loading="formLoading" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="选择供货商" prop="_Uid">
            <el-select style="width: 300px" v-model="selUser" filterable remote @change="chu()" reserve-keyword placeholder="请输入姓名或者电话号码来查找"
                       :remote-method="remoteMethod"
                       clearable :loading="loading">
                <el-option v-for="item in users" :key="item.label" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
            <!--<el-button type="primary" :disabled="disabled"  :loading="uloading"  @click="creatUU()">直接创建客户:{{uuuName}}</el-button>-->
        </el-form-item>
        <el-form-item label="付款状态" style="width:500px" prop="earnest">
            <el-radio v-model="ruleForm.earnest" label="1">结清</el-radio>
            <el-radio v-model="ruleForm.earnest" label="0">未结清</el-radio>
        </el-form-item>
        <el-form-item label="进货日期" style="width:500px" prop="inTime">
            <el-date-picker v-model="ruleForm.inTime" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
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
                    :summary-method="getSummaries"
                    show-summary
                    style="width: 100%">
                <el-table-column
                        prop="code"
                        label="商品编号"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="产品名称">
                </el-table-column>
                <el-table-column
                        width="120"
                        prop="price"
                        label="商品单价">
                </el-table-column>
                <el-table-column
                        width="100"
                        prop="unit"
                        label="产品单位">
                </el-table-column>
                <el-table-column
                        width="120"
                        label="商品进价">
                    <template slot-scope="scope">
                        <el-input
                                placeholder="请输入进价"
                                v-model="scope.row.inPrice"
                                @change="addCli(scope.row)"
                                clearable>
                        </el-input>
                    </template>
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
                //disabled:true,
                selUser:"",
                user:"",
                users:[],
                rules:{
                    _Uid: [
                        { required: true, message: '请选择供货商', trigger: 'blur' },
                    ],
                    earnest: [
                        { required: true, message: '请选择状态', trigger: 'change' }
                    ],
                    inTime: [
                        { type: 'date', required: true, message: '请选择进货日期', trigger: 'change' }
                    ],
                    date2: [
                        { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
                    ]
                },
                options5:[],
                //uloading:false,
                gloading:false,
                loading:false,
                ruleForm: {
                    _Uid:"",
                    inTime:"",//交货时间
                    earnest:"",
                },
                formLoading:false,
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
                row.amount= parseFloat((row.inPrice*row.num).toFixed(3))
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
                $.getJSON('api/product/getGoods',{name:this.name}).then(data=>{
                    this.options5=[]
                    data.list.forEach(item=>{
                        item.num=0
                        item.amount=0
                        let disabled=false
                        this.options5.push({value:JSON.stringify(item),label:item.name,disabled:disabled})
                    })
                    this.gloading=false
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.goodsTable.length==0){
                            this.$message({
                                message:'请至少选择一个商品！',
                                type: 'error'
                            });
                            return false;
                        }
                        for(let j=0,len=this.goodsTable.length;j<len;j++){
                            if(!this.isNumber(this.goodsTable[j].num)){
                                this.$message({
                                    message:'数量处应输入数字！',
                                    type: 'error'
                                });
                                return false;
                            }
                            if(!this.isNumber(this.goodsTable[j].inPrice)){
                                this.$message({
                                    message:'进价处应输入数字！',
                                    type: 'error'
                                });
                                return false;
                            }
                            if(this.goodsTable[j].num<=0){
                                this.$message({
                                    message:'数量至少为1！',
                                    type: 'error'
                                });
                                return false;
                            }
                        }
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
                                    inPrice:it.inPrice,
                                    total:it.amount,
                                }
                                detail.push(a)
                            })
                            form.inTime = (!this.ruleForm.inTime || this.ruleForm.inTime == '') ? '' : util.formatDate.format(new Date(this.ruleForm.inTime), 'yyyy-MM-dd hh:mm:ss');
                            form.detail=JSON.stringify(detail)
                            this.VgetJSON('stock/creat',form).then(data=>{
                                if(data.flag){
                                    this.$notify({
                                        title: '成功',
                                        message: data.remark,
                                        type: 'success'
                                    });
                                    this.$refs['ruleForm'].resetFields();
                                    this.selUser=""
                                    this.value10=[]
                                    this.goodsTable=[]
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
            remoteMethod(query) {
                this.loading=true
                if (query !== '') {
                    this.VgetJSON('supplier/getUsers',{name:query}).then(data=>{
                        if(data.flag){
                            this.users=data.users
                            // if(this.users.length>=1){
                            //     this.disabled=true
                            // }else {
                            //     this.uuuName=query
                            //     this.disabled=false
                            // }
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
            // creatUU(){
            //     if(this.ruleForm._Uid){
            //         this.disabled=true
            //         return
            //     }
            //     this.uloading=true
            //     let form={
            //         name:this.uuuName,
            //         phone:this.uuuName,
            //     }
            //     this.VgetJSON('supplier/creat',form).then(data=>{
            //         if(data.flag){
            //             this.$notify({
            //                 title: '创建用户成功',
            //                 message: data.remark,
            //                 type: 'success'
            //             });
            //             this.disabled=true
            //         }else {
            //             this.$message({
            //                 message: data.remark,
            //                 type: 'error'
            //             });
            //         }
            //         this.uloading=false
            //         this.remoteMethod(this.uuuName)
            //     })
            // },
            chu(){
                console.log(this.users)
                console.log(this.selUser)
                if(this.selUser){
                    this.user=this.selUser
                }else {
                    this.user={}
                }
                this.ruleForm._Uid=this.user._id
                this.ruleForm.phone=this.user.phone
                console.log("---------------")
                console.log(this.ruleForm)
            }
        },
        mounted() {
            this.getGoods()
        }
    }
</script>