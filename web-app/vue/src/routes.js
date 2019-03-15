import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Main from './views/Main.vue'
import Table from './views/nav1/Table.vue'
import Form from './views/nav2/Form.vue'
import user from './views/nav1/user.vue'
import Page4 from './views/nav2/Page4.vue'
import Page5 from './views/nav2/Page5.vue'
import end from './views/nav2/end.vue'
import Page6 from './views/nav3/Page6.vue'
import echarts from './views/charts/echarts.vue'
import comein from './views/nav4/comein.vue'
import repetory from './views/nav4/repetory.vue'
let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: Home,
        name: '客户管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/user', component: user, name: '创建客户' },
            { path: '/table', component: Table, name: '客户列表' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '订单管理',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/form', component: Form, name: '创建订单' },
            { path: '/page4', component: Page4, name: '浏览订单' },
            { path: '/page5', component: Page5, name: '生产中' },
            { path: '/end', component: end, name: '已完成' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '商品管理',
        iconCls: 'fa fa-address-card',
        // leaf: true,//只有一个节点
        children: [
            { path: '/page6', component: Page6, name: '浏览商品' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '库存管理',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/comein', component: comein, name: '进货' },
            { path: '/repetory', component:repetory, name: '库存' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: 'Charts',
        iconCls: 'fa fa-bar-chart',
        children: [
            { path: '/echarts', component: echarts, name: 'echarts' }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;