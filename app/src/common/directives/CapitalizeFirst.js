import _ from 'lodash';

// eslint-disable-next-line import/prefer-default-export
export const CapitalizeFirst = {
    created(el) {
        // eslint-disable-next-line no-param-reassign
        el.innerText = _.upperFirst(el.innerText)
    }
}
