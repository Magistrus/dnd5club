import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueMasonryPlugin } from 'vue-masonry';
import FloatingVue from 'floating-vue';
import VueEasyLightbox from 'vue-easy-lightbox';
import { vfmPlugin } from 'vue-final-modal';
import registerComponents from '@/utils/RegisterComponents';
import App from '@/App';
import router from './router';
import '@/utils/BaseScripts';
import 'swiper/css';
import 'swiper/css/bundle';
import '@/assets/styles/index.scss';

const app = createApp(App);

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
                hideTriggers: events => [...events, 'scroll'],
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
