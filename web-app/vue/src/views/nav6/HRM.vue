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
                <el-form-item>
                    <el-button size="primary" @click="handleNew()">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="users" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column prop="name" label="账户" width="220" sortable>
            </el-table-column>
            <el-table-column prop="phone" label="电话" width="140">
            </el-table-column>
            <el-table-column prop="qq" label="QQ" min-width="180">
            </el-table-column>
            <el-table-column prop="email" label="邮箱" min-width="180">
            </el-table-column>
            <el-table-column prop="level" label="管理等级" min-width="80" sortable>
            </el-table-column>
            <el-table-column prop="lastLoginTime" label="上次登陆时间" min-width="180" sortable>
            </el-table-column>
            <el-table-column label="登陆" width="150">
                <template slot-scope="scope">
                    <el-tooltip :content="'是否允许登陆' + scope.row.canLogin" placement="top">
                        <el-switch
                                v-model="scope.row.canLogin"
                                active-color="#13ce66"
                                inactive-color="#ff4949"
                                :v-loading="scc"
                                @change="sch(scope.row)"
                        >
                        </el-switch>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="toolbar">
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
            </el-pagination>
        </el-col>


        <!--编辑界面-->
        <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <h1>用户：{{editForm.name}}</h1>
                <el-form-item label="密码" prop="phone">
                    <el-input v-model="editForm.pwd" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="editForm.phone" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="QQ">
                    <el-input v-model="editForm.qq" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="editForm.email" ></el-input>
                </el-form-item>
                <el-form-item label="身份证">
                    <el-input v-model="editForm.idCardNumber"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="editForm.remark"></el-input>
                </el-form-item>
                <el-form-item label="管理等级">
                <el-select v-model="editForm.level" placeholder="请选择">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit('editForm')">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="新增" :visible.sync="newFormVisible" :close-on-click-modal="false" v-loading="addLoading">
            <el-form :model="newForm" label-width="80px" :rules="rules" ref="newForm">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="newForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="newForm.pwd" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="newForm.trueName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="newForm.phone" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="QQ">
                    <el-input v-model="newForm.qq" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="newForm.email" ></el-input>
                </el-form-item>
                <el-form-item label="身份证">
                    <el-input type="textarea" v-model="newForm.idCardNumber"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="newForm.remark"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="handleAdd('newForm')":loading="editLoading" >提交</el-button>
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
                    name:''
                },
                scc:false,
                users: [],
                total: 0,
                page: 1,
                listLoading: false,
                sels: [],//列表选中列
                newFormVisible: false,//新增界面是否显示
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' }
                    ]
                },
                rules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' }
                    ]
                },
                //编辑界面数据
                editForm: {
                    _id : "",
                    name:"",
                    pwd:"",
                    qq:"",
                    phone:"",
                    level:"",
                    idCardNumber:"",//sfz
                    email : "",//郵箱
                    remark : "",//简介开户信息
                },
                newForm: {
                    _id : "",
                    name:"",//名
                    pwd:"",
                    trueName:"",
                    phone:"",//电话
                    idCardNumber:"",//
                    email : "",//郵箱
                    remark : "",//简介
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
                    name:"",//名
                    pwd:"",
                    trueName:"",
                    phone:"",//电话
                    qq:"",
                    idCardNumber:"",//
                    email : "",//郵箱
                    remark : "",//简介
                },
                options: [{
                    value: 1,
                    label: '等级1'
                }, {
                    value:2,
                    label: '等级2'
                }, {
                    value:3,
                    label: '等级3'
                }, {
                    value:4,
                    label: '等级4'
                }
                ],
            }
        },
        created: function () {
            //页面加载时取到客户表
            // $.getJSON('/api/customer/getCustomers',{page:1},d=>{
            // 	console.log("取到用户数据")
            // 	console.log(d)
            // 	this.users=d
            // })
        },
        methods: {
            //获取用户列表
            handleCurrentChange(val) {
                this.page = val;
                this.getUsers();
            },
            getUsers() {
                let para = {
                    page: this.page,
                    name: this.filters.name
                };
                this.listLoading = true;
               this.VgetJSON('user/getUsers',para).then(d=>{
                   this.users=d.users
                   this.total=d.num
                   this.listLoading = false;
                   console.log(d)
               })
            },
            sch(row){
                this.$confirm('确认修改状态吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    let para={_id:row._id,canLogin:row.canLogin}
                    this.scc=true
                    this.VgetJSON('user/sch',para).then(data=>{
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
                        this.scc=false
                    })
                }).catch(()=>{
                    row.canLogin=!row.canLogin
                })
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = { _id: row._id };
                    this.VgetJSON('user/delete',para).then(data=>{
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
            handleNew: function () {
                this.newFormVisible = true;
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({},row);
            },
            //显示新增界面
            handleAdd: function (newForm) {
                this.$refs[newForm].validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            let para = Object.assign({}, this.newForm);
                            this.VgetJSON('user/creat',para).then(data=>{
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
                                this.newForm={
                                    _id : "",
                                    pwd:"",
                                    trueName:"",
                                    name:"",//供货商名
                                    phone:"",//供货电话
                                    address:"",//地址
                                    email : "",//郵箱
                                    remark : "",//简介开户信息
                                },
                                    //this.$refs['editForm'].resetFields();
                                    this.newFormVisible = false;
                                this.getUsers();
                                this.addLoading = false;
                            });
                        });
                    }
                });
            },
            //编辑
            editSubmit: function (editForm) {
                this.$refs[editForm].validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            let para = Object.assign({}, this.editForm);
                            this.VgetJSON('user/edit',para).then(data=>{
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
            //批量删除
            batchRemove: function () {
                var ids=[]
                ids = this.sels.map(item => item._id);
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = { ids:ids };
                    $.getJSON('api/supplier/deletes',para).then(data=>{
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

</style>