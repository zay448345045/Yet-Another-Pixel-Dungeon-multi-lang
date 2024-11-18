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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

    private static final String TXT_FOUND_SCROLL = Ml.g("levels.features.bookshelf.txt_found_scroll");
    private static final String TXT_IDENTIFY_SCROLL = Ml.g("levels.features.bookshelf.txt_identify_scroll");
    private static final String TXT_FOUND_NOTHING = Ml.g("levels.features.bookshelf.txt_found_nothing");
    private static final String TXT_FOUND_READING = Ml.g("levels.features.bookshelf.txt_found_reading");

    private static final String[] BOOKS = {

            // LORE

            Ml.g("levels.features.bookshelf.books_1"),
            Ml.g("levels.features.bookshelf.books_2"),
            Ml.g("levels.features.bookshelf.books_3"),
            Ml.g("levels.features.bookshelf.books_4"),
            Ml.g("levels.features.bookshelf.books_5"),

            // DENIZENS

            Ml.g("levels.features.bookshelf.books_6"),
            Ml.g("levels.features.bookshelf.books_7"),
            Ml.g("levels.features.bookshelf.books_8"),
            Ml.g("levels.features.bookshelf.books_9"),
            Ml.g("levels.features.bookshelf.books_10"),

            // CHARACTERS

            Ml.g("levels.features.bookshelf.books_11"),
            Ml.g("levels.features.bookshelf.books_12"),
            Ml.g("levels.features.bookshelf.books_13"),
            Ml.g("levels.features.bookshelf.books_14"),

            // CREDITS

            Ml.g("levels.features.bookshelf.books_15"),
            Ml.g("levels.features.bookshelf.books_16"),
            Ml.g("levels.features.bookshelf.books_17"),
            Ml.g("levels.features.bookshelf.books_18"),
            Ml.g("levels.features.bookshelf.books_19"),
            Ml.g("levels.features.bookshelf.books_20"),
            Ml.g("levels.features.bookshelf.books_21"),

            // CONTEST WINNERS

            Ml.g("levels.features.bookshelf.books_22"),
            Ml.g("levels.features.bookshelf.books_23"),
            Ml.g("levels.features.bookshelf.books_24"),
            Ml.g("levels.features.bookshelf.books_25"),
            Ml.g("levels.features.bookshelf.books_26"),
            Ml.g("levels.features.bookshelf.books_27"),

            // MISC

            Ml.g("levels.features.bookshelf.books_28"),
            Ml.g("levels.features.bookshelf.books_29"),


            // EXTRA CREDITS FOR YAPD LOCALIZATION
            // I added this back in 2021, Lynn and Alexstrasza helped me with YAPDCN localization while I worked on the first version of YAPDCN, hope it's alright to have us in here as extra credits
            Ml.g("levels.features.bookshelf.books_30"),
            Ml.g("levels.features.bookshelf.books_31"),
            Ml.g("levels.features.bookshelf.books_32"),

    };

    public static void examine(int cell) {

        if (Random.Float() < (0.05f + 0.05f * Dungeon.chapter())) {
            Scroll scroll = (Scroll) Generator.random(Generator.Category.SCROLL);

            if (!scroll.isTypeKnown() && Random.Int(2) == 0) {
                GLog.i(TXT_IDENTIFY_SCROLL);
                scroll.identify();
            } else {
                GLog.i(TXT_FOUND_SCROLL, scroll.name());
                Dungeon.level.drop(scroll, Dungeon.hero.pos).sprite.drop();
            }

        } else if (Random.Float() < 0.05f) {

            GLog.i(TXT_FOUND_READING, BOOKS[Random.Int(BOOKS.length)]);

        } else {

            GLog.i(TXT_FOUND_NOTHING);

        }

        Level.set(cell, Terrain.SHELF_EMPTY);
        Dungeon.observe();

        if (Dungeon.visible[cell]) {
            GameScene.updateMap(cell);
            Sample.INSTANCE.play(Assets.SND_OPEN);
        }
    }
}
