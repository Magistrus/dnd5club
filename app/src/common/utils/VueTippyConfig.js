import HTTPService from '@/common/services/HTTPService';
import errorHandler from '@/common/helpers/errorHandler';

export default {
    defaultProps: {
        content: 'Делаю запрос вселенной...',
        allowHTML: true,
        interactive: true,
        sticky: 'reference',
        theme: 'dnd5club',
        strategy: 'fixed',
        hideOnClick: false,
        onClickOutside() {
            return false
        },
        onShow(instance) {
            const ref = instance.reference;
            const http = new HTTPService();

            if (ref.getAttribute('data-tippy-url')) {
                http.rawGet(ref.getAttribute('data-tippy-url'))
                    .then(res => {
                        if (res.status !== 200) {
                            errorHandler(res.statusText);

                            return;
                        }

                        instance.setContent(res.data);
                    })
                    .catch(err => errorHandler(err));
            }
        }
    }
}
