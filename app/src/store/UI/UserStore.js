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
        registration(body = {
            username: '',
            password: '',
            email: ''
        }) {
            return new Promise((resolve, reject) => {
                if (Object.values(body).find(item => !item)) {
                    reject(new Error('All fields are required to fill'));

                    return;
                }

                http.post('/auth/signup', body)
                    .then(resp => {
                        if (resp.status !== 200) {
                            reject(resp.statusText);

                            return;
                        }

                        this.loginForm = {
                            usernameOrEmail: body.username,
                            password: body.password
                        };

                        resolve();
                    })
                    .catch(err => {
                        reject(err);
                    });
            });
        },

        authorization(body = {
            username: '',
            password: '',
            remember: false
        }) {
            this.loginForm = {
                username: '',
                password: ''
            };

            return new Promise((resolve, reject) => {
                if (Object.values(body).find(item => !item)) {
                    reject(new Error('All fields are required to fill'));

                    return;
                }

                http.post('/auth/signin', body)
                    .then(resp => {
                        if (resp.status !== 200) {
                            reject(resp.statusText);

                            return;
                        }

                        resolve();
                    })
                    .catch(err => {
                        reject(err);
                    });
            });
        }
    }
});
