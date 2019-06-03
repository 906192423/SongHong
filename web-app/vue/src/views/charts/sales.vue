<template>
    <section class="chart-container">
        <div class="container">
            <div class="block" v-loading="loading">
                <span class="demonstration">选择周</span>
                <el-date-picker
                        v-model="date"
                        type="week"
                        format="yyyy 第 WW 周"
                        @change="getCc()"
                        placeholder="选择周">
                </el-date-picker>
            </div>
        </div>
    <br>
    <el-row>
        <el-col :span="12">
            <div id="container" style="width:100%; height:400px;"></div>
        </el-col>
    </el-row>
    </section>
</template>

<script>
    import echarts from 'echarts'
    import util from '../../common/js/util'
    export default {
        data() {
            return {
                container: null,
                date:"",
                loading:false,
                list:{},
            }
        },
        methods: {
            getCc(){
                console.log(util.formatDate.format(this.date))
                let para={
                    start:util.formatDate.format(this.date)
                }
                this.VgetJSON('echart/getCc',para).then(d=>{
                    this.list=d.list
                    this.drawColumnChart()
                })
            },
            // getCash() {
            //     let para = {
            //         start:(!this.value4[0] || this.value4[0]== '') ? '' : util.formatDate.format(this.value4[0], 'yyyy-MM-dd hh:mm:ss:sss'),
            //         end:(!this.value4[1] || this.value4[1] == '') ? '' : util.formatDate.format(this.value4[1], 'yyyy-MM-dd hh:mm:ss:sss')
            //         // end: this.filters.name
            //     };
            //     this.VgetJSON('echart/getCash',para).then(d=>{
            //         console.log("取到数据")
            //         console.log(d)
            //         this.cashList=d
            //         this.drawPieChart()
            //         this. drawColumnChart()
            //     })
            // },
            drawColumnChart() {
                this.chartColumn = echarts.init(document.getElementById('container'));
                this.chartColumn.setOption({
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: ['星期一', '星期二', '星期三', '星期四', '星期五', '周六', '周日']
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: [this.list.w1, this.list.w2,this.list.w3,this.list.w4, this.list.w5, this.list.w6,this.list.w7],
                        type: 'line',
                        areaStyle: {}
                    }]
                });
            },

            drawCharts() {
                this.drawColumnChart()

            },
        },
        mounted: function () {
            this.drawCharts()
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