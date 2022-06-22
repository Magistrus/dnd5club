import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher';
import SiteLogo from '@/components/UI/SiteLogo';
import SvgIcon from '@/components/UI/SvgIcon';
import DiceRoller from '@/components/UI/DiceRoller';

/* eslint-disable vue/match-component-file-name */
export default function registerComponents(app) {
    // Components
    app.component('MenuThemeSwitcher', MenuThemeSwitcher);
    app.component('SiteLogo', SiteLogo);
    app.component('SvgIcon', SvgIcon);
    app.component('DiceRoller', DiceRoller);
}
/* eslint-enable vue/match-component-file-name */
