// eslint-disable-next-line no-unused-vars
import { RollBase, DiscordRollRenderer } from 'dice-roller-parser';

/**
 * На вход подается json ролла
 *
 * @param { RollBase } roll
 * @return { string }
 */
export const getRenderedRoll = roll => {
    const renderer = new DiscordRollRenderer();

    return renderer.render(roll);
};

export default {
    getRenderedRoll
};
