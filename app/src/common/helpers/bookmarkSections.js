import { BOOKMARK_SECTIONS } from '@/common/const/Bookmarks';

/**
 *
 * @param {string} nameOrCode - Название или код раздела
 * @return {{code: string, name: string, order: number}}
 */
export function getCategoryObj(nameOrCode) {
    return BOOKMARK_SECTIONS.find(section => section.name === nameOrCode || section.code === nameOrCode)
        || BOOKMARK_SECTIONS.find(section => section.code === 'none');
}

export default {
    getCategoryObj
};
