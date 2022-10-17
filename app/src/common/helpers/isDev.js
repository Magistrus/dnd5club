import {
    getCurrentInstance, inject, provide
} from 'vue';

const isDev = process.env.VUE_APP_DEV && process.env.VUE_APP_DEV === 'true';
const isDevInjectionKey = Symbol('isDev');

const provideIsDev = () => {
    if (getCurrentInstance()) {
        provide(isDevInjectionKey, isDev);
    }
};

const useIsDev = () => {
    const isDevInstance = getCurrentInstance()
        ? inject(isDevInjectionKey, undefined)
        : undefined;

    return isDevInstance || isDev;
};

export {
    provideIsDev,
    useIsDev
};

export default isDev;
