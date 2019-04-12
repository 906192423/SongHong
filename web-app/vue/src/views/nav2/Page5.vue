<template>
    <section>
        <el-tabs tab-position="top" style="background-color: rgba(231,202,64,0.78)" @tab-click="getInfo">
            <el-tab-pane label="生产订单查看">
                <!--工具条-->
                <el-col :span="24" class="toolbar" style="padding-bottom:0px;">
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
                <el-table :data="users" highlight-current-row v-loading="listLoading" :row-class-name="tableRowClassName" @selection-change="selsChange" style="width: 100%;"ref="multipleTable">
                    <el-table-column type="selection" width="50" :selectable='checkboxInit'>
                    </el-table-column>
                    <el-table-column type="index" width="40">
                    </el-table-column>
                    <el-table-column type="expand" >
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
                                <h1>备注：{{scope.row.remark}}</h1>
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
                    <el-table-column label="交货地址" min-width="100">
                        <template slot-scope="scope">
                            <el-popover trigger="hover" placement="top">
                                <p>{{ scope.row.addr}}</p>
                                <div slot="reference" class="name-wrapper">
                                    <el-tag size="medium" type="info" style="color: #20a0ff">交货地址</el-tag>
                                </div>
                            </el-popover>
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="联系电话" width="100">
                    </el-table-column>
                    <el-table-column label="交款方式" width="100">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.earnest==0" type="warning">定金</el-tag>
                            <el-tag v-if="scope.row.earnest==1" type="success">全款</el-tag>
                            <el-tag v-if="scope.row.earnest==-1">欠款</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="交款记录" width="100">
                        <template slot-scope="scope">
                            <div v-if="scope.row.cashList.length">
                                <el-button type="text" @click="lookCash(scope.row.cashList)">{{scope.row.cashList.length}}条记录</el-button>
                            </div>
                            <el-tag type="danger" v-if="scope.row.cashList.length==0">无</el-tag>
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
                    <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                            <!--<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>-->
                            <el-button :disabled="scope.row.cashList.length>0? true:false" type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-col :span="24" class="toolbar">
                    <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">完成定单</el-button>
                    <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
                    </el-pagination>
                </el-col>
            </el-tab-pane>
            <el-tab-pane label="生产统计" @tab-click="getInfo">
                <el-row>
                    <el-col :span="8" v-for="(o, index) in needProduct" :key="o" v-loading="carLoading">
                        <el-card :body-style="{ padding: '0px' }" class="card">
                            <img :src=img class="image">
                            <div style="padding: 14px;">
                                <span>好吃的汉堡</span>
                                <div class="bottom clearfix">
                                    <time class="time">{{ currentDate }}</time>
                                    <el-button type="text" class="button">操作按钮</el-button>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>

        <el-dialog title="交款详细信息" :visible.sync="dialog" width="1000px">
            <el-table :data="cashList" stripe highlight-current-row v-loading="csahLoading" style="width: 100%;">
                <el-table-column type="index" width="50">
                </el-table-column>
                <el-table-column type="expand">
                    <template slot-scope="scope">
                        <div>
                            <el-table
                                    :data="scope.row.payForm"
                                    style="width: 100%">
                                <el-table-column
                                        label="支付方式"
                                        width="180">
                                    <template slot-scope="scope">
                                        <el-tag v-if="scope.row.name==1" type="success">支付宝</el-tag>
                                        <el-tag v-if="scope.row.name==2" type="success">微信</el-tag>
                                        <el-tag v-if="scope.row.name==3" type="success">现金</el-tag>
                                        <el-tag v-if="scope.row.name==4" type="success">刷卡</el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="amount"
                                        label="支付金额">
                                </el-table-column>
                            </el-table>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="code" label="交易单号" width="150" sortable>
                </el-table-column>
                <el-table-column prop="cutAmount" label="优惠金额" width="110">
                </el-table-column>
                <el-table-column prop="amount" label="总金额" width="120" >
                </el-table-column>
                <el-table-column prop="ct" label="创建时间" width="200">
                </el-table-column>
                <el-table-column label="备注" width="100">
                    <template slot-scope="scope">
                        <el-popover trigger="hover" placement="top">
                            <p>{{ scope.row.remark}}</p>
                            <div slot="reference" class="name-wrapper">
                                <el-tag size="medium" type="info" style="color: #20a0ff">交易备注</el-tag>
                            </div>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column prop="creatName" label="创建人">
                </el-table-column>
                <!--<el-table-column label="操作" width="200">-->
                <!--<template slot-scope="scope">-->
                <!--<el-button size="small" @click="handleEdit(scope.row)">查看详细</el-button>-->
                <!--</template>-->
                <!--</el-table-column>-->
            </el-table>
        </el-dialog>

        <!--<el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">-->
        <!--<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">-->
        <!--<h1>订单号：{{editForm.sellCode}}</h1>-->
        <!--<h1>客户名：{{editForm.userName}}</h1>-->
        <!--<el-form-item label="交货时间" prop="leadTime">-->
        <!--<el-input v-model="editForm.leadTime" auto-complete="off"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="电话" prop="phone">-->
        <!--<el-input v-model="editForm.phone" auto-complete="off"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="生日">-->
        <!--<el-date-picker type="date" placeholder="选择日期" v-model="editForm.birth"></el-date-picker>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="身份证号" prop="idCardNumber">-->
        <!--<el-input v-model="editForm.idCardNumber" auto-complete="off"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="地址">-->
        <!--<el-input type="textarea" v-model="editForm.addr"></el-input>-->
        <!--</el-form-item>-->
        <!--</el-form>-->
        <!--<div slot="footer" class="dialog-footer">-->
        <!--<el-button @click.native="editFormVisible = false">取消</el-button>-->
        <!--<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>-->
        <!--</div>-->
        <!--</el-dialog>-->
    </section>
</template>

<script>
    import util from '../../common/js/util'
    export default {
        data() {
            return {
                currentDate: new Date(),
                filters: {
                    name: ''
                },
                img:require('../../assets/images/page4/1.jpg'),
                dialog:false,
                cashList:[],
                csahLoading:false,
                users: [],
                total: 0,
                page: 1,
                needProduct:[],
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
                // editForm: {
                //
                // },
                //
                // addFormVisible: false,//新增界面是否显示
                // addLoading: false,
                // addFormRules: {
                //     name: [
                //         { required: true, message: '请输入姓名', trigger: 'blur' }
                //     ]
                // },
                //新增界面数据
                // addForm: {
                //     name: '',
                //     sex: -1,
                //     age: 0,
                //     birth: '',
                //     addr: ''
                // }

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
            tableRowClassName({row, rowIndex}) {
                if (row.earnest!=-1&&row.cashList.length==0) {
                    return 'warning-row'
                }else {
                    return 'success-row'
                }
            },
            checkboxInit(row,index){
                if(row.earnest!=-1&&row.cashList.length==0){
                    return 0;
                }else {
                    return 1
                }
            },
            lookCash(list){
                this.dialog=true
                this.csahLoading=true
                this.VgetJSON('cash/getInfoList',{list:JSON.stringify(list)}).then(data=>{
                    this.cashList=data.list
                    this.csahLoading=false
                })
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getUsers();
            },
            //获取用户列表
            getUsers() {
                let para = {
                    page: this.page,
                    name: this.filters.name,
                    state: 1
                };
                this.listLoading = true;
                this.VgetJSON('order/getOrder',para).then(d=>{
                    this.users=d.list
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
                    let para = { _id: row._id };
                    this.VgetJSON('order/delete',para).then(data=>{
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
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
            },
            //显示新增界面
            handleAdd: function () {
                this.$router.push({ path: '/user' });
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
            //进入生产队列
            batchRemove: function () {
                var ids=[]
                ids = this.sels.map(item => item._id);
                this.$confirm('确认完成选中记录？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = { ids:ids };
                    this.VgetJSON('order/toComplete',para).then(data=>{
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
            },
            getInfo(tab, event){
                console.log(tab.index)
                if(tab.index==1){
                    this.VgetJSON('order/a',{}).then(d=>{
                        this.needProduct=d.list
                    })
                }
            }
        },
        mounted() {
            this.getUsers();
        }
    }

</script>
<style scoped>
    .card{
        margin: 20px 20px 20px 20px;
        width:404px;
        height:154px;
    }
    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .button {
        padding: 0;
        float: right;
    }

    .image {
        width: 100px;
        height: 50px;
        display: block;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }
</style>