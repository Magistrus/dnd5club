import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueMasonryPlugin } from 'vue-masonry';
import { VTooltip } from 'floating-vue';
import VueEasyLightbox from 'vue-easy-lightbox';
import { vfmPlugin } from 'vue-final-modal';
import initialScript from '@/utils/BaseScripts';
import registerComponents from '@/utils/RegisterComponents';
import router from './router';
import 'swiper/css';
import 'swiper/css/bundle';
import '@/assets/styles/index.scss';

const app = createApp({});

app.use(createPinia())
    .use(router)
    .use(VueMasonryPlugin)
    .use(VueEasyLightbox)
    .use(vfmPlugin({
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    }));

app.directive('tooltip', VTooltip);

registerComponents(app);

app.mount('#dnd5club');

initialScript();
