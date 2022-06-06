import { defineComponent, shallowRef } from 'vue';
import HTTPService from '@/services/HTTPService';

const http = new HTTPService();

async function defineRawComponent(template, url) {
    if (!template && !url) {
        throw new Error('URL and template is not defined')
    }

    try {
        let html;

        if (!template) {
            const { data } = await http.rawGet(url);

            html = data;
        }

        const component = defineComponent({
            // eslint-disable-next-line vue/match-component-file-name
            name: 'RawContent',
            template: html
        });

        return shallowRef(component);
    } catch (err) {
        return err
    }
}

export default defineRawComponent
