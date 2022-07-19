import { defineStore } from 'pinia';
import HTTPService from '@/common/services/HTTPService';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useUserStore = defineStore('UserStore', {
    state: () => ({
        loginForm: {
            usernameOrEmail: '',
            password: ''
        }
    }),

    getters: {
        getLoginForm: state => state.loginForm
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
        }
    }
});
