<template>
    <section>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect1"
                 background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
            <el-menu-item index="1">全部商品</el-menu-item>
            <el-menu-item index="2">标准商品</el-menu-item>
            <el-menu-item index="3">称重商品</el-menu-item>
            <el-menu-item index="4">组合商品</el-menu-item>
        </el-menu>
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
        <el-form :inline="true">
            <el-form-item>
                <el-input style="width: 280px" v-model="name" placeholder="输入商品名或者商品编号来查找"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" v-on:click="getProduct">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="add">添加商品</el-button>
            </el-form-item>
        </el-form>
    </el-col>

    <el-table v-loading="listLoading" :data="tableData">

        <el-table-column label="商品编号" >
            <template slot-scope="scope">
                <el-tag type="success">{{scope.row.code}}</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="商品名">
            <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                    <div slot="reference" class="name-wrapper">
                        <el-tag size="medium" type="warning">{{ scope.row.name }}</el-tag>
                    </div>
                </el-popover>
            </template>
        </el-table-column>

        <el-table-column label="单位" width="80">
            <template slot-scope="scope">
                <el-tag type="success">{{scope.row.unit}}</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="库存数量" width="80">
            <template slot-scope="scope">
                <el-tag type="success">{{scope.row.number}}</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="商品进价" width="80">
            <template slot-scope="scope">
                <el-tag type="success">{{scope.row.costPrice}}</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="商品售价" width="80">
            <template slot-scope="scope">
                <el-tag type="success">{{scope.row.price}}</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="状态" width="100">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.state==0" type="success">可生产</el-tag>
                <el-tag v-if="scope.row.state==-1" type="danger">无法生产</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="分类" width="120">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.classification==2" type="success">标准商品</el-tag>
                <el-tag v-if="scope.row.classification==3" type="danger">称重商品</el-tag>
                <el-tag v-if="scope.row.classification==4" type="success">组合商品</el-tag>
            </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
            <template slot-scope="scope">
                <el-button type="danger" size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                <!--<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>-->
            </template>
        </el-table-column>
    </el-table>
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="10":total="total" style="float:right;">
        </el-pagination>
        <!--添加商品界面-->
        <el-dialog title="添加商品" :visible.sync="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="editFormRules" ref="addForm">
                <el-form-item label="商品名" prop="name">
                    <el-input v-model="addForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品编号" prop="code">
                    <el-input v-model="addForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品进价" prop="costPrice">
                    <el-input v-model="addForm.costPrice" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品售价" prop="price">
                    <el-input v-model="addForm.price" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品单位" prop="unit">
                    <el-input v-model="addForm.unit" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="库存数量" prop="number">
                    <el-input v-model="addForm.number" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品状态">
                    <el-radio-group v-model="addForm.state">
                        <el-radio class="radio" :label="0">可生产</el-radio>
                        <el-radio class="radio" :label="-1">无法生产</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="商品分类">
                    <el-radio-group v-model="addForm.classification">
                        <el-radio class="radio" :label="2">标准商品</el-radio>
                        <el-radio class="radio" :label="3">称重商品</el-radio>
                        <el-radio class="radio" :label="4">组合商品</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="addForm.remark"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible= false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
        <!--编辑商品界面-->
        <el-dialog title="编辑商品" :visible.sync="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <el-form-item label="商品名" prop="name">
                    <el-input v-model="editForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品编号" prop="code">
                    <el-input v-model="editForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品售价" prop="price">
                    <el-input v-model="editForm.price" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="商品状态">
                    <el-radio-group v-model="editForm.state">
                        <el-radio class="radio" :label="0">可生产</el-radio>
                        <el-radio class="radio" :label="-1">无法生产</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="商品分类">
                    <el-radio-group v-model="editForm.classification">
                        <el-radio class="radio" :label="2">标准商品</el-radio>
                        <el-radio class="radio" :label="3">称重商品</el-radio>
                        <el-radio class="radio" :label="4">组合商品</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="editForm.remark"></el-input>
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
    export default {
        data() {
            return {
                activeIndex: '1',
                name:"",
                page:"1",
                total:1,
                listLoading:false,
                handleSelect:false,
                editFormVisible:false,
                editLoading:false,
                addFormVisible:false,
                addLoading:false,
                editFormRules: {
                    name: [
                        { required: true, message: '请输入名称', trigger: 'blur' }
                    ],
                    code: [
                        { required: true, message: '请输入商品编码', trigger: 'blur' }
                    ],
                    price: [
                        { required: true, message: '请输入价格', trigger: 'blur' }
                    ],
                },
                //添加商品界面数据
                addForm: {
                    name:"",//商品名
                    code:"",//商品编号
                    classification:"",//商品分类，2为标准商品，3为称重商品，4为组合商品
                    remark: "",//备注
                    price:"",//单价
                    unit:"",//单位
                    number:"",//库存数量
                    state:0,//0为可生产，-1无法生产
                    costPrice:"",//进价
                },
                //编辑商品界面数据
                editForm: {
                    id:"",
                    name:"",//商品名
                    code: "",//商品编号
                    unit:"",//单位
                    remark: "",//备注
                    price:"",//单价
                    state:"",//0为可生产，-1无法生产
                    classification:"",
                },
                tableData: []
            }
        },
        methods: {
            handleSelect1() {
                this.listLoading=true
                this.VgetJSON('product/gets',{name:this.classification,page:this.page}).then(data=>{
                    this.tableData=data.list
                    this.total=data.num
                    this.listLoading=false
                })
                this.listLoading=false
            },
            handleEdit(index, row) {
                this.editFormVisible=true
                console.log(row.state)
                this.editForm=Object.assign({},row)
            },
            editSubmit(){
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading=true
                            this.VgetJSON('product/edit',this.editForm).then(data=>{
                                if(data.flag){
                                    this.$notify({
                                        title: '成功',
                                        message: data.remark,
                                        type: 'success'
                                    });
                                    this.$refs['editForm'].resetFields();
                                    this.editFormVisible=false
                                    this.getProduct()
                                }else {
                                    this.$message({
                                        message: data.remark,
                                        type: 'error'
                                    });
                                }
                                this.editLoading=false
                            })
                        })
                    }else {
                        this.$message({
                            message:"请正确填写数据",
                            type: 'error'
                        });
                    }
                })
            },
            // handleDel(index, row) {
            //     this.$confirm('确认提交吗？', '提示', {}).then(() => {
            //         this.VgetJSON('product/delete',{_id:row._id}).then(data=>{
            //                 if(data.flag){
            //                     this.$notify({
            //                         title: '成功',
            //                         message: data.remark,
            //                         type: 'success'
            //                     });
            //                     this.getProduct()
            //                 }else {
            //                     this.$message({
            //                         message: data.remark,
            //                         type: 'error'
            //                     });
            //                 }
            //             })
            //     })
            // },
            getProduct(){
                this.listLoading=true
                this.VgetJSON('product/getGoods',{name:this.name,page:this.page}).then(data=>{
                    this.tableData=data.list
                    this.total=data.num
                    this.listLoading=false
                })
                this.listLoading=false
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getProduct();
            },
            add(){
                this.addFormVisible=true
            },
            addSubmit(){
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading=true
                            this.VgetJSON('product/creat',this.addForm).then(data=>{
                                if(data.flag){
                                    this.$notify({
                                        title: '成功',
                                        message: data.remark,
                                        type: 'success'
                                    });
                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible=false
                                    this.getProduct()
                                }else {
                                    this.$message({
                                        message: data.remark,
                                        type: 'error'
                                    });
                                }
                                this.addLoading=false
                            })
                        })
                    }else {
                        this.$message({
                            message:"请正确填写数据",
                            type: 'error'
                        });
                    }
                })
            }
        },
        mounted() {
            this.getProduct();
        }
    }
</script>