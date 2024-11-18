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
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.CheckBox;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class WndSettings extends Window {

    private static final String TXT_ZOOM_IN = Ml.g("visuals.windows.wndsettings.txt_zoom_in");
    private static final String TXT_ZOOM_OUT = Ml.g("visuals.windows.wndsettings.txt_zoom_out");
    private static final String TXT_ZOOM_DEFAULT = Ml.g("visuals.windows.wndsettings.txt_zoom_default");

    private static final String TXT_SCALE_UP = Ml.g("visuals.windows.wndsettings.txt_scale_up");
    private static final String TXT_IMMERSIVE = Ml.g("visuals.windows.wndsettings.txt_immersive");

    private static final String TXT_MUSIC = Ml.g("visuals.windows.wndsettings.txt_music");

    private static final String TXT_SOUND = Ml.g("visuals.windows.wndsettings.txt_sound");

    private static final String TXT_BUTTONS = Ml.g("visuals.windows.wndsettings.txt_buttons");

    private static final String[] TXT_BUTTONS_VAR = {
        Ml.g("visuals.windows.wndsettings.txt_buttons_var_0"),
        Ml.g("visuals.windows.wndsettings.txt_buttons_var_1"),
    };

    private static final String TXT_BRIGHTNESS = Ml.g("visuals.windows.wndsettings.txt_brightness");

    private static final String TXT_LOADING_TIPS = Ml.g("visuals.windows.wndsettings.txt_loading_tips");

    private static final String[] TXT_TIPS_DELAY = {
        Ml.g("visuals.windows.wndsettings.txt_tips_delay_0"),
        Ml.g("visuals.windows.wndsettings.txt_tips_delay_1"),
        Ml.g("visuals.windows.wndsettings.txt_tips_delay_2"),
        Ml.g("visuals.windows.wndsettings.txt_tips_delay_3"),
    };

    private static final String TXT_SEARCH_BTN = Ml.g("visuals.windows.wndsettings.txt_search_btn");

    private static final String[] TXT_SEARCH_VAR = {
        Ml.g("visuals.windows.wndsettings.txt_search_var_0"),
        Ml.g("visuals.windows.wndsettings.txt_search_var_1"),
    };

    private static final String TXT_SWITCH_PORT = Ml.g("visuals.windows.wndsettings.txt_switch_port");
    private static final String TXT_SWITCH_LAND = Ml.g("visuals.windows.wndsettings.txt_switch_land");

    private static final int WIDTH = 112;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;

    private RedButton btnZoomOut;
    private RedButton btnZoomIn;

    public WndSettings(final boolean inGame) {
        super();

        CheckBox btnImmersive = null;

        if (inGame) {
            int w = BTN_HEIGHT;

            btnZoomOut = new RedButton(TXT_ZOOM_OUT) {
                @Override
                protected void onClick() {
                    zoom(Camera.main.zoom - 1);
                }
            };
            add(btnZoomOut.setRect(0, 0, w, BTN_HEIGHT));

            btnZoomIn = new RedButton(TXT_ZOOM_IN) {
                @Override
                protected void onClick() {
                    zoom(Camera.main.zoom + 1);
                }
            };
            add(btnZoomIn.setRect(WIDTH - w, 0, w, BTN_HEIGHT));

            add(new RedButton(TXT_ZOOM_DEFAULT) {
                @Override
                protected void onClick() {
                    zoom(PixelScene.defaultZoom);
                }
            }.setRect(btnZoomOut.right(), 0, WIDTH - btnZoomIn.width() - btnZoomOut.width(), BTN_HEIGHT));

            updateEnabled();

            RedButton btnSearchBtn = new RedButton(searchButtonsText(YetAnotherPixelDungeon.searchButton())) {
                @Override
                protected void onClick() {

                    boolean val = !YetAnotherPixelDungeon.searchButton();

                    YetAnotherPixelDungeon.searchButton(val);

                    text.text(searchButtonsText(val));
                    text.measure();
                    layout();
                }
            };
            btnSearchBtn.setRect(0, BTN_HEIGHT + GAP, WIDTH, BTN_HEIGHT);
            add(btnSearchBtn);

            CheckBox btnBrightness = new CheckBox(TXT_BRIGHTNESS) {
                @Override
                protected void onClick() {
                    super.onClick();
                    YetAnotherPixelDungeon.brightness(checked());
                }
            };
            btnBrightness.setRect(0, btnSearchBtn.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnBrightness.checked(YetAnotherPixelDungeon.brightness());
            add(btnBrightness);


        } else {

            RedButton btnOrientation = new RedButton(orientationText()) {
                @Override
                protected void onClick() {
                    YetAnotherPixelDungeon.landscape(!YetAnotherPixelDungeon.landscape());
                }
            };
            btnOrientation.setRect(0, 0, WIDTH, BTN_HEIGHT);
            add(btnOrientation);

            CheckBox btnScaleUp = new CheckBox(TXT_SCALE_UP) {
                @Override
                protected void onClick() {
                    super.onClick();
                    YetAnotherPixelDungeon.scaleUp(checked());
                }
            };

            btnScaleUp.setRect(0, btnOrientation.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnScaleUp.checked(YetAnotherPixelDungeon.scaleUp());
            add(btnScaleUp);

            btnImmersive = new CheckBox(TXT_IMMERSIVE) {
                @Override
                protected void onClick() {
                    super.onClick();
                    YetAnotherPixelDungeon.immerse(checked());
                }
            };
            btnImmersive.setRect(0, btnScaleUp.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnImmersive.checked(YetAnotherPixelDungeon.immersed());
            btnImmersive.enable(android.os.Build.VERSION.SDK_INT >= 19);
            add(btnImmersive);

        }

        CheckBox btnMusic = new CheckBox(TXT_MUSIC) {
            @Override
            protected void onClick() {
                super.onClick();
                YetAnotherPixelDungeon.music(checked());
            }
        };
        btnMusic.setRect(0, (BTN_HEIGHT + GAP) * 3, WIDTH, BTN_HEIGHT);
        btnMusic.checked(YetAnotherPixelDungeon.music());
        add(btnMusic);

        CheckBox btnSound = new CheckBox(TXT_SOUND) {
            @Override
            protected void onClick() {
                super.onClick();
                YetAnotherPixelDungeon.soundFx(checked());
                Sample.INSTANCE.play(Assets.SND_CLICK);
            }
        };
        btnSound.setRect(0, btnMusic.bottom() + GAP, WIDTH, BTN_HEIGHT);
        btnSound.checked(YetAnotherPixelDungeon.soundFx());
        add(btnSound);

//        RedButton btnTracks = new RedButton( buttonsText( YetAnotherPixelDungeon.buttons() ) ) {
//            @Override
//            protected void onClick() {
//                super.onClick();
//
//                boolean val = !YetAnotherPixelDungeon.buttons();
//
//                YetAnotherPixelDungeon.buttons( val );
//
//                Sample.INSTANCE.play( Assets.SND_CLICK );
//
//                text.text( buttonsText( val ) );
//                text.measure();
//                layout();
//            }
//        };
//
//        btnTracks.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
//        add(btnTracks);

        RedButton btnTipsDelay = new RedButton(loadingTipsText(YetAnotherPixelDungeon.loadingTips())) {
            @Override
            protected void onClick() {

                int val = YetAnotherPixelDungeon.loadingTips();

                val = val < 3 ? val + 1 : 0;
                YetAnotherPixelDungeon.loadingTips(val);

                text.text(loadingTipsText(val));
                text.measure();
                layout();
            }
        };

//        btnTipsDelay.setRect(0, btnTracks.bottom() + GAP, WIDTH, BTN_HEIGHT);
        btnTipsDelay.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
        add(btnTipsDelay);

        resize(WIDTH, (int) btnTipsDelay.bottom());


//			CheckBox btnQuickslot = new CheckBox( TXT_QUICKSLOT ) {
//				@Override
//				protected void onClick() {
//					super.onClick();
//					Toolbar.secondQuickslot(checked());
//				}
//			};
//			btnQuickslot.setRect( 0, btnBrightness.bottom() + GAP, WIDTH, BTN_HEIGHT );
//			btnQuickslot.checked( Toolbar.secondQuickslot() );
//			add( btnQuickslot );

//			resize( WIDTH, (int)btnQuickslot.bottom() );

    }

    private void zoom(float value) {

        Camera.main.zoom(value);
        YetAnotherPixelDungeon.zoom((int) (value - PixelScene.defaultZoom));

        updateEnabled();
    }

    private void updateEnabled() {
        float zoom = Camera.main.zoom;
        btnZoomIn.enable(zoom < PixelScene.maxZoom);
        btnZoomOut.enable(zoom > PixelScene.minZoom);
    }

    private String orientationText() {
        return YetAnotherPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND;
    }

    private String searchButtonsText(boolean val) {
        return Utils.format(TXT_SEARCH_BTN, TXT_SEARCH_VAR[val ? 1 : 0]);
    }

    private String loadingTipsText(int val) {
        return Utils.format(TXT_LOADING_TIPS, TXT_TIPS_DELAY[val]);
    }

    private String buttonsText(boolean val) {
        return Utils.format(TXT_BUTTONS, TXT_BUTTONS_VAR[val ? 1 : 0]);
    }
}
