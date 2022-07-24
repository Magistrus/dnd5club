import { defineStore } from 'pinia';
import HTTPService from '@/common/services/HTTPService';
import Cookies from 'js-cookie';

const http = new HTTPService();

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
        async registration(body = {
            username: '',
            password: '',
            email: ''
        }) {
            try {
                if (Object.values(body).find(item => !item)) {
                    return Promise.reject(new Error('All fields are required to fill'));
                }

                const resp = await http.post('/auth/signup', body);

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

                const resp = await http.post('/auth/signin', config);

                switch (resp.status) {
                    case 200:
                        await this.getUserFromSession();

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

        async getUserInfo(username) {
            try {
                const resp = await http.post(`/profile/${ username }`);

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
                if (!Cookies.get('dnd5_user_token')) {
                    return Promise.resolve();
                }

                const resp = await http.post('/user');

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
        }
    }
});
