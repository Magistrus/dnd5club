import axios from 'axios';
import Cookies from 'js-cookie';

export default class HTTPService {
    constructor() {
        this.instance = axios.create({
            baseURL: `${ process.env.VUE_APP_API_URL || '' }/api/v1`,
            withCredentials: true
        });

        this.instanceRaw = axios.create({
            baseURL: process.env.VUE_APP_API_URL || '',
            withCredentials: true
        });
    }

    post(url, data, signal = new AbortController().signal) {
        const config = {
            url,
            data,
            signal,
            method: 'post',
            headers: {}
        };

        if (Cookies.get('dnd5_user_token')) {
            config.headers.Authorization = `Bearer ${ Cookies.get('dnd5_user_token') }`;
        }

        return this.instance(config);
    }

    get(url, params) {
        const config = {
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get',
            headers: {}
        };

        if (Cookies.get('dnd5_user_token')) {
            config.headers.Authorization = `Bearer ${ Cookies.get('dnd5_user_token') }`;
        }

        return this.instance(config);
    }

    rawGet(url, params) {
        return this.instanceRaw({
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get'
        });
    }
}
