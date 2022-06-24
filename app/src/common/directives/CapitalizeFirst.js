import { upperFirst } from 'lodash/fp';

// eslint-disable-next-line import/prefer-default-export
export const CapitalizeFirst = {
    created(el) {
        // eslint-disable-next-line no-param-reassign
        el.innerText = upperFirst(el.innerText)
    }
}
