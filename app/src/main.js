import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueMasonryPlugin } from 'vue-masonry';
import FloatingVue from 'floating-vue';
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
    .use(FloatingVue, {
        instantMove: true,
        disposeTimeout: 0,
        themes: {
            tooltip: {
                delay: {
                    show: 200,
                    hide: 0,
                },
                handleResize: true,
                html: true,
                loadingContent: 'Посылаем запрос вселенной...',
            }
        }
    })
    .use(vfmPlugin({
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    }));

registerComponents(app);

app.mount('#dnd5club');

initialScript();
