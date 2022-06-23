import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueMasonryPlugin } from 'vue-masonry';
import VueEasyLightbox from 'vue-easy-lightbox';
import VueTippy from 'vue-tippy/dist/vue-tippy';
import { vfmPlugin } from 'vue-final-modal';
import registerComponents from '@/utils/RegisterComponents';
import HTTPService from '@/services/HTTPService';
import VueTippyConfig from '@/utils/VueTippyConfig';
import App from '@/App';
import router from './router';
import '@/utils/BaseScripts';
import 'swiper/css';
import 'swiper/css/bundle';
import '@/assets/styles/index.scss';

const app = createApp(App);

app.config.globalProperties.$http = new HTTPService();

app.use(createPinia())
    .use(router)
    .use(VueMasonryPlugin)
    .use(VueEasyLightbox)
    .use(VueTippy, VueTippyConfig)
    .use(vfmPlugin({
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    }));

registerComponents(app);

app.mount('#dnd5club');
