import Login from "../views/Login.vue";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            title: '登录',
            requiresAuth: false
        }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// router.beforeEach(async (to,from,next) => {
//     document.title = to.meta.title ? `${to.meta.title}-即时聊天` : `即时聊天`
//
//     if (to.meta.requiresAuth === false) {
//         next()
//         return
//     }
// })

export default router