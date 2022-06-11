import ClassesView from '@/views/Character/Classes/ClassesView';
import SpellsView from '@/views/Spells/SpellsView';
import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher';
import SiteLogo from '@/components/UI/SiteLogo';
import RacesView from '@/views/Character/Races/RacesView';
import TraitsView from '@/views/Character/Traits/TraitsView';
import BackgroundsView from '@/views/Character/Backgrounds/BackgroundsView';
import OptionsView from '@/views/Character/Options/OptionsView';
import SvgIcon from '@/components/UI/SvgIcon';
import DiceRoller from '@/components/UI/DiceRoller';
import WeaponsView from '@/views/Inventory/Weapons/WeaponsView';

export default function registerComponents(app) {
    /* eslint-disable vue/match-component-file-name */
    // Views
    app.component('ClassesView', ClassesView);
    app.component('RacesView', RacesView);
    app.component('TraitsView', TraitsView);
    app.component('BackgroundsView', BackgroundsView);
    app.component('OptionsView', OptionsView);
    app.component('SpellsView', SpellsView);
    app.component('WeaponsView', WeaponsView);

    // Components
    app.component('MenuThemeSwitcher', MenuThemeSwitcher);
    app.component('SiteLogo', SiteLogo);
    app.component('SvgIcon', SvgIcon);
    app.component('DiceRoller', DiceRoller);
    /* eslint-enable vue/match-component-file-name */
}
