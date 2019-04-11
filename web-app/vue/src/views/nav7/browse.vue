<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="输入姓名或者电话来查找"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getUsers">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="users" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;"ref="multipleTable">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
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
                                    label="退价">
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
            <el-table-column prop="code" label="退货单号" width="150" sortable>
            </el-table-column>
            <el-table-column prop="userName" label="客户姓名" width="200" sortable>
            </el-table-column>
            <el-table-column prop="amount" label="总金额" width="150"  sortable>
            </el-table-column>
            <el-table-column prop="ct" label="退货时间" width="200" sortable>
            </el-table-column>
            <el-table-column prop="creatName" label="创建者" sortable>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="toolbar">
            <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">进入生产队列</el-button>-->
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <h1>订单号：{{editForm.code}}</h1>
                <h1>客户名：{{editForm._Uid}}</h1>
                <el-form-item label="交货时间" prop="leadTime">
                    <el-input v-model="editForm.leadTime" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="editForm.phone" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="生日">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.birth"></el-date-picker>
                </el-form-item>
                <el-form-item label="身份证号" prop="idCardNumber">
                    <el-input v-model="editForm.idCardNumber" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input type="textarea" v-model="editForm.addr"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    import util from '../../common/js/util'
    export default {
        data() {
            return {
                filters: {
                    name: ''
                },
                users: [],
                total: 0,
                page: 1,
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
                editForm: {
                    sellCode: "",//订单号
                    detail:[
//                        [
//                                _id: "",
//                                name:"",
//                                code: "",
//                                num:0,
//                                price:0,
//                        ],
                    ],
                    userName : "",//客户姓名
                    _Uid: "",//客户id
                    leadTime: "",//交货时间
                    remark : "",//备注
                    amount :"0",//合计金额
                    payState:"",//0结清，-1未结清

                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' }
                    ]
                },
                //新增界面数据
                addForm: {
                    name: '',
                    sex: -1,
                    age: 0,
                    birth: '',
                    addr: ''
                }

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
            handleCurrentChange(val) {
                this.page = val;
                this.getUsers();
            },
            //获取用户列表
            getUsers() {
                let para = {
                    page: this.page,
                    name: this.filters.name,
                };
                this.listLoading = true;
                this.VgetJSON('exchange/getOrder',para).then(d=>{
                    this.users=d.list
                    this.total=d.num
                    this.listLoading = false;
                })
            },
            //删除

            //显示编辑界面

            //显示新增界面
            //编辑
            // editSubmit: function () {
            //     this.$refs.editForm.validate((valid) => {
            //         if (valid) {
            //             this.$confirm('确认提交吗？', '提示', {}).then(() => {
            //                 this.editLoading = true;
            //                 let para = Object.assign({}, this.editForm);
            //                 para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
            //                 this.VgetJSON('customer/edit',para).then(data=>{
            //                     this.editLoading = false;
            //                     if(data.flag){
            //                         this.$message({
            //                             message:data.remark,
            //                             type: 'success'
            //                         });
            //                     }else {
            //                         this.$message({
            //                             message: data.remark,
            //                             type: 'error'
            //                         });
            //                     }
            //                     this.$refs['editForm'].resetFields();
            //                     this.editFormVisible = false;
            //                     this.getUsers();
            //                 });
            //             });
            //         }
            //     });
            // },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //进入生产队列
            batchRemove: function () {
                var ids=[]
                ids = this.sels.map(item => item._id);
                this.$confirm('确认转移选中记录到生产队列？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = { ids:ids };
                    this.VgetJSON('order/toProduct',para).then(data=>{
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
            }
        },
        mounted() {
            this.getUsers();
        }
    }

</script>

<style scoped>
    .table_c {
        width: 100%;
        height: 500px;
        background: url("../../assets/images/page4/1.jpg") center
        center no-repeat;
        background-size: 100% auto;
        padding: 10px;
    }
</style>