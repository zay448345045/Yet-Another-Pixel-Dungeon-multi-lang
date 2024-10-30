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

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

    private static final int WIDTH_P = 128;
    private static final int HEIGHT_P = 160;

    private static final int WIDTH_L = 160;
    private static final int HEIGHT_L = 128;

    private static final String TXT_TITLE = Ml.g("visuals.windows.wndchangelog.txt_title");

    private static final String TXT_DESCR = Ml.g("visuals.windows.wndchangelog.txt_descr");

    private BitmapText txtTitle;
    private ScrollPane list;

    public WndChangelog() {

        super();

        if (YetAnotherPixelDungeon.landscape()) {
            resize(WIDTH_L, HEIGHT_L);
        } else {
            resize(WIDTH_P, HEIGHT_P);
        }

        txtTitle = PixelScene.createText(TXT_TITLE, 9);
        txtTitle.hardlight(Window.TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = PixelScene.align(PixelScene.uiCamera, (width - txtTitle.width()) / 2);
        add(txtTitle);

        list = new ScrollPane(new ChangelogItem(TXT_DESCR, width, txtTitle.height()));
        add(list);

        list.setRect(0, txtTitle.height(), width, height - txtTitle.height());
        list.scrollTo(0, 0);

    }

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        private BitmapTextMultiline normal;
        private BitmapTextMultiline highlighted;

        public ChangelogItem(String text, int width, float offset) {
            super();

//            label.text( text );
//            label.maxWidth = width;
//            label.measure();

            Highlighter hl = new Highlighter(text);

//            normal = PixelScene.createMultiline( hl.text, 6 );
            normal.text(hl.text);
            normal.maxWidth = width;
            normal.measure();
//            normal.x = 0;
//            normal.y = offset;
//            add( normal );

            if (hl.isHighlighted()) {
                normal.mask = hl.inverted();

//                highlighted = PixelScene.createMultiline( hl.text, 6 );
                highlighted.text(hl.text);
                highlighted.maxWidth = normal.maxWidth;
                highlighted.measure();
//                highlighted.x = normal.x;
//                highlighted.y = normal.y;
//                add( highlighted );

                highlighted.mask = hl.mask;
                highlighted.hardlight(TITLE_COLOR);
            }

            height = normal.height() + GAP;
        }

        @Override
        protected void createChildren() {
            normal = PixelScene.createMultiline(6);
            add(normal);
            highlighted = PixelScene.createMultiline(6);
            add(highlighted);
        }

        @Override
        protected void layout() {
            normal.y = PixelScene.align(y + GAP);
            highlighted.y = PixelScene.align(y + GAP);
        }
    }
}
