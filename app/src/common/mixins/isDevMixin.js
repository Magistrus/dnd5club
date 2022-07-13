import isDevMode from '@/common/helpers/isDevMode';

export default {
    computed: {
        isDev() {
            return isDevMode();
        }
    }
};
