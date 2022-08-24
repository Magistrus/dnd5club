import { defineStore } from 'pinia';
import Cookies from 'js-cookie';
import { USER_TOKEN_COOKIE } from '@/common/const/UI';

// eslint-disable-next-line import/prefer-default-export
export const useUserStore = defineStore('UserStore', {
    state: () => ({
        user: undefined,
        status: false
    }),

    getters: {
        getUser: state => state.user,
        isAuthenticated: state => state.status
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

                const resp = await this.$http.post('/auth/signup', body);

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

                const resp = await this.$http.post('/auth/signin', config);

                switch (resp.status) {
                    case 200:
                        if (this.$isDev) {
                            Cookies.set(
                                USER_TOKEN_COOKIE,
                                resp.data.accessToken,
                                {
                                    expires: 365
                                }
                            );
                        }

                        await this.getUserInfo();

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
                const resp = await this.$http.post('/auth/signout');

                switch (resp.status) {
                    case 200:
                        if (this.$isDev) {
                            Cookies.remove(USER_TOKEN_COOKIE, { path: '' });
                        }

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
            this.status = false;
            this.user = undefined;

            Cookies.remove(USER_TOKEN_COOKIE);
        },

        async getUserInfo() {
            try {
                if (!await this.getUserStatus()) {
                    return Promise.reject(new Error('User is not authenticated'));
                }

                const resp = await this.$http.get('/user/info');

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

        async getUserStatus() {
            try {
                const resp = await this.$http.get('/user/status');

                switch (resp.status) {
                    case 200:
                        this.status = true;

                        return Promise.resolve(true);
                    default:
                        this.clearUser();

                        return Promise.resolve(false);
                }
            } catch (err) {
                this.clearUser();

                return Promise.resolve(false);
            }
        }
    }
});
