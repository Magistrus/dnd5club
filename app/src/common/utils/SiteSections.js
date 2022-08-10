import HTTPService from '@/common/services/HTTPService';

export default class SiteSections {
    constructor() {
        this.$http = new HTTPService();
    }

    async getSections() {
        try {
            const resp = await this.$http.post('/bookmarks/sections');

            if (resp.status !== 200) {
                return Promise.reject(resp.statusText);
            }

            return Promise.resolve(resp.data);
        } catch (err) {
            return Promise.reject(err);
        }
    }

    getDefault() {}
}
