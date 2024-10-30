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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.Challenges;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.CheckBox;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.BitmapText;

import java.util.ArrayList;

public class WndChallenges extends Window {

    private static final int WIDTH = 108;
    private static final int TTL_HEIGHT = 12;
    private static final int BTN_HEIGHT = 18;
    private static final int GAP = 1;

    private static final String TITLE = Ml.g("visuals.windows.wndchallenges.title");

    private boolean editable;
    private ArrayList<CheckBox> boxes;

    public WndChallenges(int checked, boolean editable) {

        super();

        this.editable = editable;

        BitmapText title = PixelScene.createText(TITLE, 9);
        title.hardlight(TITLE_COLOR);
        title.measure();
        title.x = PixelScene.align(camera, (WIDTH - title.width()) / 2);
        title.y = PixelScene.align(camera, (TTL_HEIGHT - title.height()) / 2);
        add(title);

        boxes = new ArrayList<CheckBox>();

        float pos = TTL_HEIGHT;
        for (int i = 0; i < Challenges.NAMES.length; i++) {

            CheckBox cb = new CheckBox(Challenges.NAMES[i]);
            cb.checked((checked & Challenges.MASKS[i]) != 0);
            cb.active = editable;

            if (i > 0) {
                pos += GAP;
            }
            cb.setRect(0, pos, WIDTH, BTN_HEIGHT);
            pos = cb.bottom();

            add(cb);
            boxes.add(cb);
        }

        resize(WIDTH, (int) pos);
    }

    @Override
    public void onBackPressed() {

        if (editable) {
            int value = 0;
            for (int i = 0; i < boxes.size(); i++) {
                if (boxes.get(i).checked()) {
                    value |= Challenges.MASKS[i];
                }
            }
            YetAnotherPixelDungeon.challenges(value);
        }

        super.onBackPressed();
    }
}
