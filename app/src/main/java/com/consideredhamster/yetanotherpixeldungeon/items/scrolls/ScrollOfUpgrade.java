/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

    private static final String TXT_KNOWN_UPGRADED = Ml.g("items.scrolls.scrollofupgrade.txt_known_upgraded");
    private static final String TXT_KNOWN_REPAIRED = Ml.g("items.scrolls.scrollofupgrade.txt_known_repaired");

    private static final String TXT_UNKNW_REPAIRED = Ml.g("items.scrolls.scrollofupgrade.txt_unknw_repaired");
    private static final String TXT_UNKNW_WHOKNOWS = Ml.g("items.scrolls.scrollofupgrade.txt_unknw_whoknows");

    private static final String TXT_CURSE_WEAKENED = Ml.g("items.scrolls.scrollofupgrade.txt_curse_weakened");
    private static final String TXT_CURSE_DISPELLED = Ml.g("items.scrolls.scrollofupgrade.txt_curse_dispelled");

    {
        name = "Scroll of Upgrade";
        shortName = "Up";

        inventoryTitle = "Select an item to upgrade";
        mode = WndBag.Mode.UPGRADEABLE;

        spellSprite = SpellSprite.SCROLL_UPGRADE;
        spellColour = SpellSprite.COLOUR_HOLY;
    }

    @Override
    protected void onItemSelected(Item item) {

        item.identify(CURSED_KNOWN);

        if (item.bonus >= 0) {

            if (item.isIdentified()) {
                GLog.p(item.bonus < 3 ? TXT_KNOWN_UPGRADED : TXT_KNOWN_REPAIRED, item.name());
            } else {
                GLog.p(item.state < 3 ? TXT_UNKNW_REPAIRED : TXT_UNKNW_WHOKNOWS, item.name());
            }

            item.upgrade();
            curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);

        } else {

            item.upgrade();

            if (item.bonus < 0) {

                GLog.w(TXT_CURSE_WEAKENED, item.name());
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

            } else {

                GLog.p(TXT_CURSE_DISPELLED, item.name());
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

            }
        }


        item.repair(1);

        QuickSlot.refresh();

        Statistics.itemsUpgraded++;

        Badges.validateItemsUpgraded();
    }


    @Override
    public String desc() {
        return Ml.g("items.scrolls.scrollofupgrade.desc");
    }

    @Override
    public int price() {
        return isTypeKnown() ? 125 * quantity : super.price();
    }
}
