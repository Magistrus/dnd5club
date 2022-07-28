import { defineStore } from 'pinia';
import Cookies from 'js-cookie';
import axios from 'axios';
import cloneDeep from 'lodash/cloneDeep';

const COOKIE_NAME = 'dnd5_user_token';

// eslint-disable-next-line import/prefer-default-export
export const useUserStore = defineStore('UserStore', {
    state: () => ({
        user: undefined
    }),

    getters: {
        getUser: state => state.user,
        isAuthorized: state => !!state.user
    },

    actions: {
        http() {
            return axios.create({
                baseURL: `${ process.env.VUE_APP_API_URL || '' }/api/v1`,
                withCredentials: true,
                headers: {},
                transformRequest: [
                    (data, headers) => {
                        if (Cookies.get(COOKIE_NAME)) {
                            // eslint-disable-next-line no-param-reassign
                            headers.Authorization = `Bearer ${ Cookies.get(COOKIE_NAME) }`;
                        }

                        return cloneDeep(data);
                    }
                ],
                validateStatus: status => {
                    if (status === 401) {
                        this.clearUser();
                    }

                    return status >= 200 && status < 300;
                }
            });
        },

        async registration(body = {
            username: '',
            password: '',
            email: ''
        }) {
            try {
                if (Object.values(body).find(item => !item)) {
                    return Promise.reject(new Error('All fields are required to fill'));
                }

                const resp = await this.http().post('/auth/signup', body);

                switch (resp.status) {
                    case 200:
                        return Promise.resolve();
                    default:
                        return Promise.reject(resp.statusText);
                }
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async authorization(body = {
            usernameOrEmail: '',
            password: '',
            remember: false
        }) {
            try {
                const config = {
                    usernameOrEmail: '',
                    password: '',
                    remember: false,
                    ...body
                };

                if (Object.values(body).find(item => !item)) {
                    return Promise.reject(new Error('All fields are required to fill'));
                }

                const resp = await this.http().post('/auth/signin', config);

                switch (resp.status) {
                    case 200:
                        await this.updateUserFromSession();

                        return Promise.resolve();
                    case 401:
                        // eslint-disable-next-line prefer-promise-reject-errors
                        return Promise.reject('Неверный логин или пароль');
                    default:
                        return Promise.reject(resp.statusText);
                }
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async logout() {
            try {
                const resp = await this.http().post('/auth/signout');

                switch (resp.status) {
                    case 200:
                        this.clearUser();

                        return Promise.resolve();
                    default:
                        return Promise.reject();
                }
            } catch (err) {
                return Promise.reject(err);
            }
        },

        clearUser() {
            this.user = undefined;

            Cookies.remove(COOKIE_NAME);
        },

        async getUserInfo(username) {
            try {
                const resp = await this.http().post(`/profile/${ username }`);

                switch (resp.status) {
                    case 200:
                        this.user = resp.data;

                        return Promise.resolve(resp.data);
                    default:
                        return Promise.reject(resp.statusText);
                }
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async updateUserFromSession() {
            try {
                const resp = await this.http().post('/user');

                switch (resp.status) {
                    case 200:
                        this.user = resp.data;

                        return Promise.resolve(resp.data);
                    default:
                        this.clearUser();

                        return Promise.resolve();
                }
            } catch (err) {
                this.clearUser();

                return Promise.resolve();
            }
        }
    }
});
