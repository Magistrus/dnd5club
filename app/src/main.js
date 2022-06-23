import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueMasonryPlugin } from 'vue-masonry';
import VueEasyLightbox from 'vue-easy-lightbox';
import VueTippy from 'vue-tippy/dist/vue-tippy';
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
    .use(VueTippy, {
        directive: 'tippy',
        defaultProps: {
            allowHTML: true,
            interactive: true,
            sticky: true,
            theme: 'dnd5club',
            strategy: 'fixed',
        }
    })
    .use(vfmPlugin({
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    }));

registerComponents(app);

app.mount('#dnd5club');
