import axios from 'axios';

export default class HTTPService {
    constructor() {
        this.instance = axios.create({
            baseURL: `${ process.env.VUE_APP_API_URL || '' }/api/v1`
        });

        this.instanceRaw = axios.create({
            baseURL: process.env.VUE_APP_API_URL || ''
        });
    }

    post(url, data, signal = new AbortController().signal) {
        return this.instance({
            url,
            data,
            signal,
            method: 'post'
        });
    }

    get(url, params) {
        return this.instance({
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get'
        });
    }

    rawGet(url, params) {
        return this.instanceRaw({
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get'
        });
    }
}
