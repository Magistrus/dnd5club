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

    async post(url, data, signal) {
        return this.instance({
            url,
            data,
            signal,
            method: 'post',
        })
    }

    async get(url, params) {
        return this.instance({
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get',
        })
    }

    async rawGet(url, params) {
        return this.instanceRaw({
            url,
            params: new URLSearchParams(params).toString(),
            method: 'get',
        })
    }
}
