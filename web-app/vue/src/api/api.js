import $ from "jquery"
import 'es6-promise/auto'
import vue from "../main.js"
let base = 'api';
exports.install = function (Vue, options) {
    Vue.prototype.Vpost= function (url,params){
        return new Promise(function (resolve, reject) {
            $.post(`${base}/${url}`, params).then(res=>{
                console.log("1111111111111")
                let data=JSON.parse(res)
                if(data.OnLine&&data.OnLine){
                    console.log("未登陆")
                    sessionStorage.removeItem('user')
                    vue.$message({
                        message: res.remark,
                        type: 'error'
                    });
                    vue.$router.push({ path: '/login' })
                }else {
                }
                console.log("0000")
                resolve(res)
            })
        })
    };
    Vue.prototype.VgetJSON= function (url,params){
        return new Promise(function (resolve, reject) {
            $.getJSON(`${base}/${url}`, params).then(res=>{
                console.log("JSON22222222222")
                if(res.OnLine&&res.OnLine){
                    console.log("未登陆")
                    sessionStorage.removeItem('user')
                    vue.$message({
                        message: res.remark,
                        type: 'error'
                    });
                    vue.$router.push({ path: '/login' })
                }else {
                }
                console.log("0000")
                resolve(res)
            })
        })
    }
}
// export const Vpost=(url,params)=> {
//     return $.post(`${base}/${url}`, params).then(res=>{
//         console.log(res)
//         console.log(res.state)
//         if(res.state=605)
//             sessionStorage.removeItem('user')
//         this.$router.push('/login')
//     });
// };
// export const VgetJSON =(url,params)=> {
//     return $.getJSON(`${base}/${url}`,params).then(res=>{
//         console.log(res)
//         console.log(res.state)
//         if(res.state=605)
//             sessionStorage.removeItem('user')
//         this.$router.push('/login')
//     });
// };
