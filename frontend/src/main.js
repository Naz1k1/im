import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/index.js";
import ElementPlus from "element-plus";
import 'element-plus/dist/index.css'
import "./styles/Global.css"
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './styles/theme.css'
const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(router)
app.mount('#app')
