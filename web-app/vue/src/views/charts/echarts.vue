<template>
    <section class="chart-container">
        <div v-loading="Loading">
                <span class="demonstration">选择日期：</span>
                <el-date-picker
                        @change="getCash()"
                        v-model="value4"
                        type="datetimerange"
                        :picker-options="pickerOptions2"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        align="right">
                </el-date-picker>
            <span class="demonstration">选择销售员：</span>
            <el-select v-model="value" placeholder="请选择" @change="getCash()">
                <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-table :data="[cashList]" style="width: 100%">
                <el-table-column prop="all" label="统计收款单数量" width="180">
                </el-table-column>
                <el-table-column  label="总收款" width="180">
                    <template slot-scope="scope">
                        {{scope.row.w+scope.row.x+scope.row.z+scope.row.k}}
                    </template>
                </el-table-column>
                <el-table-column prop="x" label="现金收款" width="180">
                </el-table-column>
                <el-table-column prop="w" label="微信收款"width="180">
                </el-table-column>
                <el-table-column prop="z" label="支付宝收款"width="180">
                </el-table-column>
                <el-table-column prop="k" label="刷卡收款"width="180">
                </el-table-column>
            </el-table>
            <p>支出：{{am}}</p>
        </div>
<br>
        <el-row>
            <el-col :span="12">
                <div id="chartColumn" style="width:100%; height:400px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="chartPie" style="width:100%; height:400px;"></div>
            </el-col>
            <!--<el-col :span="12">-->
                <!--<div id="chartLine" style="width:100%; height:400px;"></div>-->
            <!--</el-col>-->
            <!--<el-col :span="12">-->
                <!--<div id="chartBar"  style="width:100%; height:400px;"></div>-->
            <!--</el-col>-->
            <!--<el-col :span="24">-->
            <!--</el-col>-->
        </el-row>

    </section>

</template>

<script>
    import echarts from 'echarts'
    import util from '../../common/js/util'
    export default {
        data() {
            return {
                Loading:false,
                value: '0',
                am:'0',
                options: [
                    {
                        value: '0',
                        label: '全部'
                    }
                    ],
                pickerOptions2: {
                    shortcuts: [{
                        text: '当天',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setHours(0,0,0,0)
                            end.setHours(0,0,0,0)
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setHours(0,0,0,0)
                            end.setHours(0,0,0,0)
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setHours(0,0,0,0)
                            end.setHours(0,0,0,0)
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                cashList:{
                    w:0,
                    z:0,
                    x:0,
                    k:0,
                    all:0,
                },
                value4:[ this.b(new Date()),
                    this.b(new Date(new Date().getTime()+(24 * 60 * 60 * 1000)))],
                chartColumn: null,
                chartBar: null,
                chartLine: null,
                chartPie: null
            }
        },

        methods: {
            b(d){
                d.setHours(0,0,0,0)
                return d
            },
            getCash() {
                let para = {
                    start:(!this.value4[0] || this.value4[0]== '') ? '' : util.formatDate.format(this.value4[0], 'yyyy-MM-dd hh:mm:ss:sss'),
                    end:(!this.value4[1] || this.value4[1] == '') ? '' : util.formatDate.format(this.value4[1], 'yyyy-MM-dd hh:mm:ss:sss')
                    // end: this.filters.name
                };
                para.uid=this.value
                this.Loading = true
                this.VgetJSON('echart/getCash',para).then(d=>{
                    this.cashList=d.c
                    this.am=d.b
                    this.drawPieChart()
                    this. drawColumnChart()
                    this.Loading = false
                })
            },
            getUsers() {
                this.VgetJSON('User/getSmallUsers',"").then(d=>{
                    console.log("取到数据")
                    console.log(d)
                    let us=[
                        {
                            value: '0',
                            label: '全部'
                        }
                    ]
                    d.users.forEach((it)=>{
                        let u={
                            value:it._id,
                            label:it.trueName
                        }
                        us.push(u)
                    })
                    this.options=us
                })
            },
            drawColumnChart() {
                this.chartColumn = echarts.init(document.getElementById('chartColumn'));
                this.chartColumn.setOption({
                  title: {
                      text: '解款报表柱状图',
                      subtext: '各种支付方式收款统计',},
                  tooltip: {},
                  xAxis: {
                      data: ["现金", "支付宝", "微信", "银联刷卡"]
                  },
                  yAxis: {},
                  series: [{
                      name: '销量',
                      type: 'bar',
                      data: [this.cashList.x, this.cashList.z, this.cashList.w,this.cashList.k]
                    }]
                });
            },
            drawBarChart() {
                this.chartBar = echarts.init(document.getElementById('chartBar'));
                this.chartBar.setOption({
                    title: {
                        text: 'Bar Chart',
                        subtext: '数据来自网络'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        data: ['2011年', '2012年']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        boundaryGap: [0, 0.01]
                    },
                    yAxis: {
                        type: 'category',
                        data: ['巴西', '印尼', '美国', '印度', '中国', '世界人口(万)']
                    },
                    series: [
                        {
                            name: '2011年',
                            type: 'bar',
                            data: [18203, 23489, 29034, 104970, 131744, 630230]
                        },
                        {
                            name: '2012年',
                            type: 'bar',
                            data: [19325, 23438, 31000, 121594, 134141, 681807]
                        }
                    ]
                });
            },
            drawLineChart() {
                this.chartLine = echarts.init(document.getElementById('chartLine'));
                this.chartLine.setOption({
                    title: {
                        text: 'Line Chart'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['邮件营销', '联盟广告', '搜索引擎']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: '邮件营销',
                            type: 'line',
                            stack: '总量',
                            data: [120, 132, 101, 134, 90, 230, 210]
                        },
                        {
                            name: '联盟广告',
                            type: 'line',
                            stack: '总量',
                            data: [220, 182, 191, 234, 290, 330, 310]
                        },
                        {
                            name: '搜索引擎',
                            type: 'line',
                            stack: '总量',
                            data: [820, 932, 901, 934, 1290, 1330, 1320]
                        }
                    ]
                });
            },
            drawPieChart() {
                this.chartPie = echarts.init(document.getElementById('chartPie'));
                this.chartPie.setOption({
                    title: {
                        text: '解款报表饼图',
                        subtext: '各种支付方式收款统计',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['现金支付', '微信收款', '支付宝收款', '银联刷卡',]
                    },
                    series: [
                        {
                            name: '访问来源',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            data: [
                                { value: this.cashList.x, name: '现金支付' },
                                { value: this.cashList.w, name: '微信收款' },
                                { value: this.cashList.z, name: '支付宝收款' },
                                { value: this.cashList.k, name: '银联刷卡' },
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawCharts() {
                this.drawColumnChart()
                this.drawPieChart()
            },
        },

        mounted: function () {
            this.drawCharts()
            this.getCash()
            this.getUsers()

        },
        updated: function () {
            this.drawCharts()
        }
    }
</script>

<style scoped>
    .chart-container {
        width: 100%;
        float: left;
    }
    /*.chart div {
        height: 400px;
        float: left;
    }*/

    .el-col {
        padding: 30px 20px;
    }
</style>
