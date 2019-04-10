<template>
    <div>
    <div class="page" id="printMe" style="background:#fff;">
        <table border="1">
            <tr class="tr-1">
                <td><div>单位名称</div></td>
                <td>
                    <div class="input"></div>
                </td>
                <td><div>统一社会信用代码</div></td>
                <td>
                    <div class="input"></div>
                </td>
            </tr>
            <tr class="tr-2">
                <td colspan="1"><div>吊销范围</div></td>
                <td colspan="9">
                    <div>

                    </div>
                </td>
            </tr>
            <tr class="tr-3">
                <td colspan="1"><div>吊销原因</div></td>
                <td colspan="9">
                    <div>

                    </div>
                </td>
            </tr>
            <tr class="tr-4">
                <td colspan="1" rowspan="4"><div>吊销来源</div></td>
                <td colspan="9" rowspan="4">
                    <div class="check-box">
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <el-button v-print="'#printMe'">打印页面</el-button>
    </div>
</template>

<script>
    export default {
        name: "stamp"
    }
</script>

<style scoped>
    .page {
        width: 21cm;
        min-height: 29.7cm;
        padding: 2cm;
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
</style>