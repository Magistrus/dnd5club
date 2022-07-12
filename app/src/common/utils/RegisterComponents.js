import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher';
import SiteLogo from '@/components/UI/SiteLogo';
import SvgIcon from '@/components/UI/SvgIcon';
import DiceRoller from '@/components/UI/DiceRoller';
import NavBar from '@/components/UI/menu/NavBar';
import RawContent from '@/components/content/RawContent';
import DetailTooltip from '@/components/UI/DetailTooltip';

/* eslint-disable vue/match-component-file-name */
export default function registerComponents(app) {
    // Components
    app.component('MenuThemeSwitcher', MenuThemeSwitcher);
    app.component('SiteLogo', SiteLogo);
    app.component('SvgIcon', SvgIcon);
    app.component('DiceRoller', DiceRoller);
    app.component('NavBar', NavBar);
    app.component('RawContent', RawContent);
    app.component('DetailTooltip', DetailTooltip);
}
/* eslint-enable vue/match-component-file-name */
