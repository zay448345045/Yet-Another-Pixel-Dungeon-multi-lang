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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

public class WndTutorial extends WndTabbed {

    private static final int WIDTH_P = 112;
    private static final int HEIGHT_P = 160;

    private static final int WIDTH_L = 128;
    private static final int HEIGHT_L = 128;

    private static SmartTexture icons;
    private static TextureFilm film;

    private static final String TXT_TITLE = Ml.g("visuals.windows.wndtutorial.txt_title");

        private static final String[] TXT_LABELS = {
                Ml.g("visuals.windows.wndtutorial.txt_labels_1"),
                Ml.g("visuals.windows.wndtutorial.txt_labels_2"),
                Ml.g("visuals.windows.wndtutorial.txt_labels_3"),
                Ml.g("visuals.windows.wndtutorial.txt_labels_4"),
                Ml.g("visuals.windows.wndtutorial.txt_labels_5"),
        };

        private static final String[] TXT_TITLES = {
                Ml.g("visuals.windows.wndtutorial.txt_titles_1"),
                Ml.g("visuals.windows.wndtutorial.txt_titles_2"),
                Ml.g("visuals.windows.wndtutorial.txt_titles_3"),
                Ml.g("visuals.windows.wndtutorial.txt_titles_4"),
                Ml.g("visuals.windows.wndtutorial.txt_titles_5"),
        };

        private static final String[][] TXT_POINTS = {
                {
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_1"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_2"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_3"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_4"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_5"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_6"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_7"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_8"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_9"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_10"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_11"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_1_12"),
                },
                {
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_1"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_2"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_3"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_4"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_5"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_6"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_7"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_8"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_9"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_10"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_11"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_2_12"),
                },
                {
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_1"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_2"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_3"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_4"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_5"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_6"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_7"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_8"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_9"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_10"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_11"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_3_12"),
                },
                {
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_1"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_2"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_3"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_4"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_5"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_6"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_7"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_8"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_9"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_10"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_11"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_4_12"),
                },
                {
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_1"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_2"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_3"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_4"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_5"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_6"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_7"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_8"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_9"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_10"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_11"),
                        Ml.g("visuals.windows.wndtutorial.txt_points_5_12"),
                }
        };

    private BitmapText txtTitle;
    private ScrollPane list;

    private enum Tabs {

        INTERFACE,
        MECHANICS,
        CONSUMABLES,
        EQUIPMENT,
        DENIZENS,

    }

    ;

//	private ArrayList<Component> items = new ArrayList<>();

    private static Tabs currentTab;

    public WndTutorial() {

        super();

        icons = TextureCache.get(Assets.HELP);
        film = new TextureFilm(icons, 24, 24);

        if (YetAnotherPixelDungeon.landscape()) {
            resize(WIDTH_L, HEIGHT_L);
        } else {
            resize(WIDTH_P, HEIGHT_P);
        }

        txtTitle = PixelScene.createText(TXT_TITLE, 9);
        txtTitle.hardlight(Window.TITLE_COLOR);
        txtTitle.measure();
        add(txtTitle);

        list = new ScrollPane(new Component());
        add(list);
        list.setRect(0, txtTitle.height(), width, height - txtTitle.height());

        Tab[] tabs = {
                new LabeledTab(TXT_LABELS[0]) {
                    @Override
                    protected void select(boolean value) {
                        super.select(value);

                        if (value) {
                            currentTab = Tabs.INTERFACE;
                            updateList(TXT_TITLES[0]);
                        }
                    }

                    ;
                },
                new LabeledTab(TXT_LABELS[1]) {
                    @Override
                    protected void select(boolean value) {
                        super.select(value);

                        if (value) {
                            currentTab = Tabs.MECHANICS;
                            updateList(TXT_TITLES[1]);
                        }
                    }

                    ;
                },
                new LabeledTab(TXT_LABELS[2]) {
                    @Override
                    protected void select(boolean value) {
                        super.select(value);

                        if (value) {
                            currentTab = Tabs.EQUIPMENT;
                            updateList(TXT_TITLES[2]);
                        }
                    }

                    ;
                },
                new LabeledTab(TXT_LABELS[3]) {
                    @Override
                    protected void select(boolean value) {
                        super.select(value);

                        if (value) {
                            currentTab = Tabs.CONSUMABLES;
                            updateList(TXT_TITLES[3]);
                        }
                    }

                    ;
                },
                new LabeledTab(TXT_LABELS[4]) {
                    @Override
                    protected void select(boolean value) {
                        super.select(value);

                        if (value) {
                            currentTab = Tabs.DENIZENS;
                            updateList(TXT_TITLES[4]);
                        }
                    }

                    ;
                },
        };

        int tabWidth = (width + 12) / tabs.length;

        for (Tab tab : tabs) {
            tab.setSize(tabWidth, tabHeight());
            add(tab);
        }

        select(0);
    }

    private void updateList(String title) {

        txtTitle.text(title);
        txtTitle.measure();
        txtTitle.x = PixelScene.align(PixelScene.uiCamera, (width - txtTitle.width()) / 2);

//		items.clear();

        Component content = list.content();
        content.clear();
        list.scrollTo(0, 0);

        int index = 0;
        float pos = 0;

        switch (currentTab) {

            case INTERFACE:
                for (String text : TXT_POINTS[0]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case MECHANICS:

                index += 12;

                for (String text : TXT_POINTS[1]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case EQUIPMENT:

                index += 24;

                for (String text : TXT_POINTS[2]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case CONSUMABLES:

                index += 36;

                for (String text : TXT_POINTS[3]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case DENIZENS:

                index += 48;

                for (String text : TXT_POINTS[4]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

        }

        content.setSize(width, pos);
    }

    private static class TutorialItem extends Component {

        private final int GAP = 4;
        private Image icon;
        private BitmapTextMultiline label;

        public TutorialItem(String text, int index, int width) {
            super();

            icon.frame(film.get(index));

            label.text(text);
            label.maxWidth = width - (int) icon.width() - GAP;
            label.measure();

            height = Math.max(icon.height(), label.height()) + GAP;
        }

        @Override
        protected void createChildren() {

            icon = new Image(icons);
            add(icon);

            label = PixelScene.createMultiline(6);
            add(label);
            layout();
        }

        @Override
        protected void layout() {
            icon.y = PixelScene.align(y);

            label.x = icon.x + icon.width;
            label.y = PixelScene.align(y);
            label.layout();
        }
    }
}
