export default function isDev() {
    return process.env.VUE_APP_DEV && process.env.VUE_APP_DEV === 'true';
}
