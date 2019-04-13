<template>
    <el-row>
        <el-col :span="12">
            <div id="container" style="width:100%; height:400px;"></div>
        </el-col>
    </el-row>
</template>

<script>
    import echarts from 'echarts'
    export default {
        data() {
            return {
                container: null,
            }
        },

        methods: {

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
                        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: [820, 932, 901, 934, 1290, 1330, 1320],
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