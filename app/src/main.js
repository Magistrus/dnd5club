import { createApp } from 'vue';
import { createPinia } from 'pinia';
import VueEasyLightbox from 'vue-easy-lightbox';
import VueTippy from 'vue-tippy/dist/vue-tippy';
import VueLazyload from 'vue-lazyload';
import vfmPlugin from 'vue-final-modal';
import isDev from '@/common/helpers/isDev';
import registerComponents from '@/common/utils/RegisterComponents';
import HTTPService from '@/common/services/HTTPService';
import VueTippyConfig from '@/common/utils/VueTippyConfig';
import App from '@/App';
import router from './router';
import '@/common/utils/BaseScripts';
import '@/assets/styles/index.scss';

const app = createApp(App);

app.config.globalProperties.$http = new HTTPService();
app.config.globalProperties.$isDev = isDev;

const pinia = createPinia();

pinia.use(({ store }) => {
    // eslint-disable-next-line no-param-reassign
    store.$http = new HTTPService();
});

app.use(pinia)
    .use(router)
    .use(VueEasyLightbox)
    .use(VueTippy, VueTippyConfig)
    .use(VueLazyload, {
        preLoad: 1.7
    })
    .use(vfmPlugin, {
        key: '$vfm',
        componentName: 'VueFinalModal',
        dynamicContainerName: 'ModalsContainer'
    });

registerComponents(app);

app.mount('#dnd5club');
