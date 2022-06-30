import { createApp } from 'vue';
import { createPinia } from 'pinia';
import VueEasyLightbox from 'vue-easy-lightbox';
import VueTippy from 'vue-tippy/dist/vue-tippy';
import VueLazyload from 'vue-lazyload';
import VScrollLock from '@/common/directives/VScrollLock';
import { vfmPlugin } from 'vue-final-modal';
import registerComponents from '@/common/utils/RegisterComponents';
import HTTPService from '@/common/services/HTTPService';
import VueTippyConfig from '@/common/utils/VueTippyConfig';
import App from '@/App';
import router from './router';
import '@/common/utils/BaseScripts';
import 'swiper/css';
import 'swiper/css/bundle';
import '@/assets/styles/index.scss';

const app = createApp(App);

app.config.globalProperties.$http = new HTTPService();

app.use(createPinia())
    .use(router)
    .use(VueEasyLightbox)
    .use(VueTippy, VueTippyConfig)
    .use(VueLazyload)
    .use(VScrollLock, {
        reserveScrollBarGap: true
    })
    .use(vfmPlugin({
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    }));

registerComponents(app);

app.mount('#dnd5club');
