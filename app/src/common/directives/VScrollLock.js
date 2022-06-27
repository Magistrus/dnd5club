import { disableBodyScroll, enableBodyScroll } from 'body-scroll-lock';

const bslOptions = {
    allowTouchMove: undefined,
    reserveScrollBarGap: false
}

export default {
    install: (app, options) => {
        if (options) {
            for (const [key, value] of Object.entries(options)) {
                if (key in bslOptions) {
                    bslOptions[key] = value
                }
            }
        }

        app.directive('scroll-lock', {
            mounted(el, binding) {
                if (binding.value) {
                    disableBodyScroll(el, bslOptions)
                }
            },

            updated(el, binding) {
                if (binding.value) {
                    disableBodyScroll(el, bslOptions)
                } else {
                    enableBodyScroll(el)
                }
            },

            unmounted(el) {
                enableBodyScroll(el)
            }
        })
    },
}
