import HTTPService from '@/common/services/HTTPService';
import errorHandler from '@/common/helpers/errorHandler';

export default {
    defaultProps: {
        allowHTML: true,
        interactive: true,
        hideOnClick: false,
        sticky: true,
        theme: 'dnd5club',
        strategy: 'fixed',
        maxWidth: 450,
        delay: [700, null],
        interactiveBorder: 7,
        touch: ['hold', 500],
        appendTo: () => document.body,
        onClickOutside() {
            return false
        },
        onShow(instance) {
            const ref = instance.reference;

            let canShow = false;

            if (ref.getAttribute('data-tippy-url')) {
                canShow = true;
            }

            if (instance.props.content) {
                canShow = true;
            }

            if (!canShow) {
                return canShow;
            }

            const http = new HTTPService();

            if (ref.getAttribute('data-tippy-url')) {
                http.rawGet(ref.getAttribute('data-tippy-url'))
                    .then(res => {
                        if (res.status !== 200) {
                            errorHandler(res.statusText);

                            canShow = false;
                        }

                        instance.setContent(res.data);

                        canShow = true;
                    })
                    .catch(err => {
                        errorHandler(err);

                        canShow = false;
                    });
            }

            return canShow;
        }
    }
}
