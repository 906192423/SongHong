<template>
    <el-form :model="dynamicValidateForm" :rules="rules" ref="dynamicValidateForm" style="width: 90%" class="demo-dynamic">
        <el-form-item label="请选择订单" prop="order">
        <el-autocomplete
                v-model="state4"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入订单号"
                @select="handleSelect"
        ></el-autocomplete>
        </el-form-item>
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
            <el-table-column prop="sellCode" label="订单号" width="120" sortable>
            </el-table-column>
            <el-table-column prop="userName" label="消费者" width="120" sortable>
            </el-table-column>
            <el-table-column prop="amount" label="总金额" width="120" sortable>
            </el-table-column>
            <el-table-column prop="leadTime" label="交货时间" width="220" sortable>
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
            <el-table-column prop="phone" label="联系电话" width="220" sortable>
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
            <el-table-column prop="creatName" label="销售员" width="120" sortable>
            </el-table-column>
        </el-table>
        <el-form-item
                v-for="(domain, index) in dynamicValidateForm.domains"
                :label="'域名' + index"
                :key="domain.key"
                :prop="'domains.' + index + '.value'"
                :rules="{required: true, message: '域名不能为空', trigger: 'blur'}"
        >
            <el-input v-model="domain.value"></el-input><el-button @click.prevent="removeDomain(domain)">删除</el-button>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>
            <el-button @click="addDomain">新增域名</el-button>
            <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        data() {
            return {
                state4:'',
                order:[],
                rules:{
                    order: [
                        { required: true, message: '请选择订单号', trigger: 'blur' },
                    ],
                },
                dynamicValidateForm: {
                    order:"",
                    domains: [{
                        value: ''
                    }],
                    email: ''
                }
            };
        },
        methods: {
            querySearchAsync(queryString, cb) {
                $.getJSON("api/order/cheOrder",{code:queryString}).then(data=>{
                    cb(data.list)
                })
            },
            handleSelect(item) {
                console.log(item);
                this.dynamicValidateForm.order=item.item._id
                this.order=[item.item]
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            removeDomain(item) {
                var index = this.dynamicValidateForm.domains.indexOf(item)
                if (index !== -1) {
                    this.dynamicValidateForm.domains.splice(index, 1)
                }
            },
            addDomain() {
                this.dynamicValidateForm.domains.push({
                    value: '',
                    key: Date.now()
                });
            }
        }
    }
</script>

<style scoped>

</style>