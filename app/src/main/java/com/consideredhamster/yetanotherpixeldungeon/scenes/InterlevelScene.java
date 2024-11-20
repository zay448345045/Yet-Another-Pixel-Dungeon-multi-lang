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
package com.consideredhamster.yetanotherpixeldungeon.scenes;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndError;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndStory;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.io.Console;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class InterlevelScene extends PixelScene {

    private static final float TIME_TO_FADE = 0.5f;

    private static final String TXT_DESCENDING = Ml.g("scenes.interlevelscene.txt_descending");
    private static final String TXT_ASCENDING = Ml.g("scenes.interlevelscene.txt_ascending");
    private static final String TXT_LOADING = Ml.g("scenes.interlevelscene.txt_loading");
    private static final String TXT_RESURRECTING = Ml.g("scenes.interlevelscene.txt_resurrecting");
    private static final String TXT_RETURNING = Ml.g("scenes.interlevelscene.txt_returning");
    private static final String TXT_FALLING = Ml.g("scenes.interlevelscene.txt_falling");
    private static final String TXT_CONTINUE = Ml.g("scenes.interlevelscene.txt_continue");

    private static final String ERR_FILE_NOT_FOUND = Ml.g("scenes.interlevelscene.err_file_not_found");
    private static final String ERR_GENERIC = Ml.g("scenes.interlevelscene.err_generic");

    public static enum Mode {
        DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
    }

    ;
    public static Mode mode;

    public static int returnDepth;
    public static int returnPos;

    public static boolean noStory = false;

    public static boolean fallIntoPit;

    private static final String[] TIPS = {

            // GENERAL

            Ml.g("scenes.interlevelscene.tips_1"),
            Ml.g("scenes.interlevelscene.tips_2"),
            Ml.g("scenes.interlevelscene.tips_3"),
            Ml.g("scenes.interlevelscene.tips_4"),
            Ml.g("scenes.interlevelscene.tips_5"),
            Ml.g("scenes.interlevelscene.tips_6"),
            Ml.g("scenes.interlevelscene.tips_7"),
            Ml.g("scenes.interlevelscene.tips_8"),
            Ml.g("scenes.interlevelscene.tips_9"),
            Ml.g("scenes.interlevelscene.tips_10"),
            Ml.g("scenes.interlevelscene.tips_11"),
            Ml.g("scenes.interlevelscene.tips_12"),
            Ml.g("scenes.interlevelscene.tips_13"),
            Ml.g("scenes.interlevelscene.tips_14"),
            Ml.g("scenes.interlevelscene.tips_15"),
            Ml.g("scenes.interlevelscene.tips_16"),
            Ml.g("scenes.interlevelscene.tips_17"),
            Ml.g("scenes.interlevelscene.tips_18"),
            Ml.g("scenes.interlevelscene.tips_19"),
            Ml.g("scenes.interlevelscene.tips_20"),

            // WEAPONS & ARMOURS

            Ml.g("scenes.interlevelscene.tips_21"),
            Ml.g("scenes.interlevelscene.tips_22"),
            Ml.g("scenes.interlevelscene.tips_23"),
            Ml.g("scenes.interlevelscene.tips_24"),
            Ml.g("scenes.interlevelscene.tips_25"),
            Ml.g("scenes.interlevelscene.tips_26"),
            Ml.g("scenes.interlevelscene.tips_27"),
            Ml.g("scenes.interlevelscene.tips_28"),
            Ml.g("scenes.interlevelscene.tips_29"),
            Ml.g("scenes.interlevelscene.tips_30"),
            Ml.g("scenes.interlevelscene.tips_31"),
            Ml.g("scenes.interlevelscene.tips_32"),

            // WANDS & RINGS

            Ml.g("scenes.interlevelscene.tips_33"),
            Ml.g("scenes.interlevelscene.tips_34"),
            Ml.g("scenes.interlevelscene.tips_35"),
            Ml.g("scenes.interlevelscene.tips_36"),
            Ml.g("scenes.interlevelscene.tips_37"),
            Ml.g("scenes.interlevelscene.tips_38"),
            Ml.g("scenes.interlevelscene.tips_39"),
            Ml.g("scenes.interlevelscene.tips_40"),
            Ml.g("scenes.interlevelscene.tips_41"),
            Ml.g("scenes.interlevelscene.tips_42"),
            Ml.g("scenes.interlevelscene.tips_43"),
            Ml.g("scenes.interlevelscene.tips_44"),
            Ml.g("scenes.interlevelscene.tips_45"),
            Ml.g("scenes.interlevelscene.tips_46"),
            Ml.g("scenes.interlevelscene.tips_47"),
            Ml.g("scenes.interlevelscene.tips_48"),
            Ml.g("scenes.interlevelscene.tips_49"),
            Ml.g("scenes.interlevelscene.tips_50"),
            Ml.g("scenes.interlevelscene.tips_51"),

            // POTIONS

            Ml.g("scenes.interlevelscene.tips_52"),
            Ml.g("scenes.interlevelscene.tips_53"),
            Ml.g("scenes.interlevelscene.tips_54"),
            Ml.g("scenes.interlevelscene.tips_55"),
            Ml.g("scenes.interlevelscene.tips_56"),
            Ml.g("scenes.interlevelscene.tips_57"),
            Ml.g("scenes.interlevelscene.tips_58"),
            Ml.g("scenes.interlevelscene.tips_59"),
            Ml.g("scenes.interlevelscene.tips_60"),
            Ml.g("scenes.interlevelscene.tips_61"),
            Ml.g("scenes.interlevelscene.tips_62"),
            Ml.g("scenes.interlevelscene.tips_63"),
            Ml.g("scenes.interlevelscene.tips_64"),
            Ml.g("scenes.interlevelscene.tips_65"),
            Ml.g("scenes.interlevelscene.tips_66"),
            Ml.g("scenes.interlevelscene.tips_67"),
            Ml.g("scenes.interlevelscene.tips_68"),
            Ml.g("scenes.interlevelscene.tips_69"),
            Ml.g("scenes.interlevelscene.tips_70"),
            Ml.g("scenes.interlevelscene.tips_71"),
            Ml.g("scenes.interlevelscene.tips_72"),
            Ml.g("scenes.interlevelscene.tips_73"),
            Ml.g("scenes.interlevelscene.tips_74"),
            Ml.g("scenes.interlevelscene.tips_75"),
            Ml.g("scenes.interlevelscene.tips_76"),
            Ml.g("scenes.interlevelscene.tips_77"),
            Ml.g("scenes.interlevelscene.tips_78"),
            Ml.g("scenes.interlevelscene.tips_79"),

            // SCROLLS

            Ml.g("scenes.interlevelscene.tips_80"),
            Ml.g("scenes.interlevelscene.tips_81"),
            Ml.g("scenes.interlevelscene.tips_82"),
            Ml.g("scenes.interlevelscene.tips_83"),
            Ml.g("scenes.interlevelscene.tips_84"),
            Ml.g("scenes.interlevelscene.tips_85"),
            Ml.g("scenes.interlevelscene.tips_86"),
            Ml.g("scenes.interlevelscene.tips_87"),
            Ml.g("scenes.interlevelscene.tips_88"),
            Ml.g("scenes.interlevelscene.tips_89"),
            Ml.g("scenes.interlevelscene.tips_90"),
            Ml.g("scenes.interlevelscene.tips_91"),
            Ml.g("scenes.interlevelscene.tips_92"),
            Ml.g("scenes.interlevelscene.tips_93"),
            Ml.g("scenes.interlevelscene.tips_94"),
            Ml.g("scenes.interlevelscene.tips_95"),
            Ml.g("scenes.interlevelscene.tips_96"),
            Ml.g("scenes.interlevelscene.tips_97"),
            Ml.g("scenes.interlevelscene.tips_98"),
            Ml.g("scenes.interlevelscene.tips_99"),
            Ml.g("scenes.interlevelscene.tips_100"),
            Ml.g("scenes.interlevelscene.tips_101"),
            Ml.g("scenes.interlevelscene.tips_102"),
            Ml.g("scenes.interlevelscene.tips_103"),

            // FOOD

            Ml.g("scenes.interlevelscene.tips_104"),
            Ml.g("scenes.interlevelscene.tips_105"),
            Ml.g("scenes.interlevelscene.tips_106"),
            Ml.g("scenes.interlevelscene.tips_107"),
            Ml.g("scenes.interlevelscene.tips_108"),
            Ml.g("scenes.interlevelscene.tips_109"),
            Ml.g("scenes.interlevelscene.tips_110"),
            Ml.g("scenes.interlevelscene.tips_111"),
            Ml.g("scenes.interlevelscene.tips_112"),
            Ml.g("scenes.interlevelscene.tips_113"),
            Ml.g("scenes.interlevelscene.tips_114"),
            Ml.g("scenes.interlevelscene.tips_115"),

            // BOSSES

            Ml.g("scenes.interlevelscene.tips_116"),
            Ml.g("scenes.interlevelscene.tips_117"),
            Ml.g("scenes.interlevelscene.tips_118"),
            Ml.g("scenes.interlevelscene.tips_119"),
            Ml.g("scenes.interlevelscene.tips_120"),
            Ml.g("scenes.interlevelscene.tips_121"),
            Ml.g("scenes.interlevelscene.tips_122"),
            Ml.g("scenes.interlevelscene.tips_123"),
            Ml.g("scenes.interlevelscene.tips_124"),
            Ml.g("scenes.interlevelscene.tips_125"),

            // TERRAIN

            Ml.g("scenes.interlevelscene.tips_126"),
            Ml.g("scenes.interlevelscene.tips_127"),
            Ml.g("scenes.interlevelscene.tips_128"),
            Ml.g("scenes.interlevelscene.tips_129"),
            Ml.g("scenes.interlevelscene.tips_130"),
            Ml.g("scenes.interlevelscene.tips_131"),
            Ml.g("scenes.interlevelscene.tips_132")
    };

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

    private Phase phase;
    private float timeLeft;

    private BitmapText message;
    private ArrayList<BitmapText> tipBox;

    private Thread thread;
    private String error = null;
    private boolean pause = false;

    @Override
    public void create() {
        super.create();

        String text = "";
//        int depth = Dungeon.depth;

        switch (mode) {
            case DESCEND:
                text = TXT_DESCENDING;
//                depth++;
                break;
            case ASCEND:
                text = TXT_ASCENDING;
//                depth--;
                break;
            case CONTINUE:
                text = TXT_LOADING;

//                GamesInProgress.Info info = GamesInProgress.check( StartScene.curClass );

//                if (info != null) {
//
//                    depth = info.depth;
//
//                }

//                depth = depth > 0 ? depth : 0 ;

                break;
            case RESURRECT:
                text = TXT_RESURRECTING;
                break;
            case RETURN:
                text = TXT_RETURNING;
//                depth = returnDepth;
                break;
            case FALL:
                text = TXT_FALLING;
//                depth++;
                break;
        }

        message = PixelScene.createText(text, 10);
        message.measure();
        message.x = (Camera.main.width - message.width()) / 2;
        message.y = (Camera.main.height - message.height()) / 2;
        add(message);

        tipBox = new ArrayList<>();

        if (YetAnotherPixelDungeon.loadingTips() > 0) {

            BitmapTextMultiline tip = PixelScene.createMultiline(TIPS[Random.Int(TIPS.length)], 6);
            tip.maxWidth = Camera.main.width * 9 / 10;
            tip.measure();

            tip.x = PixelScene.align(Camera.main.width / 2 - tip.width() / 2);
            tip.y = PixelScene.align(Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * tip.height());
            add(tip);
        }


        phase = Phase.FADE_IN;
        timeLeft = TIME_TO_FADE;

        thread = new Thread() {
            @Override
            public void run() {

                try {

                    Generator.reset();

                    switch (mode) {
                        case DESCEND:
                            descend();
                            break;
                        case ASCEND:
                            ascend();
                            break;
                        case CONTINUE:
                            restore();
                            break;
//					case RESURRECT:
//                        resurrect();
//                        break;
                        case RETURN:
                            returnTo();
                            break;
                        case FALL:
                            fall();
                            break;
                    }

                    if ((Dungeon.depth % 6) == 0 && Dungeon.depth == Statistics.deepestFloor) {
                        Sample.INSTANCE.load(Assets.SND_BOSS);
                    }

                    if (mode != Mode.CONTINUE) {
                        Dungeon.saveAll();
                        Badges.saveGlobal();
                    }

                } catch (FileNotFoundException e) {

                    error = ERR_FILE_NOT_FOUND;

                } catch (Exception e) {

                    error = e.toString();
                    YetAnotherPixelDungeon.reportException(e);

                }

//                error = ERR_FILE_NOT_FOUND;

                if (phase == Phase.STATIC && error == null) {
                    phase = Phase.FADE_OUT;
                    timeLeft = TIME_TO_FADE * 2;
                }
            }
        };
        thread.start();
    }

    @Override
    public void update() {
        super.update();

        float p = timeLeft / TIME_TO_FADE;

        switch (phase) {

            case FADE_IN:

                message.alpha(1 - p);

                for (BitmapText line : tipBox) {
                    line.alpha(1 - p);
                }

                if ((timeLeft -= Game.elapsed) <= 0) {
                    if (thread.isAlive() || error != null || YetAnotherPixelDungeon.loadingTips() > 2) {
                        phase = Phase.STATIC;

                        if (!thread.isAlive() && error == null) {
                            message.text(TXT_CONTINUE);
                            message.measure();
                            message.x = (Camera.main.width - message.width()) / 2;
                            message.y = (Camera.main.height - message.height()) / 2;

                            TouchArea hotArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height) {
                                @Override
                                protected void onClick(Touchscreen.Touch touch) {
                                    phase = Phase.FADE_OUT;
                                    timeLeft = TIME_TO_FADE;
                                    this.destroy();
                                }
                            };
                            add(hotArea);
                        }

                    } else {
                        phase = Phase.FADE_OUT;
                        timeLeft = (YetAnotherPixelDungeon.loadingTips() > 0 ?
                                TIME_TO_FADE * YetAnotherPixelDungeon.loadingTips() * 3 : TIME_TO_FADE);
                    }
                }
                break;

            case FADE_OUT:

                message.alpha(p);

                for (BitmapText line : tipBox) {
                    line.alpha(p);
                }

                if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
                    Music.INSTANCE.volume(p);
                }
                if ((timeLeft -= Game.elapsed) <= 0) {
                    Game.switchScene(GameScene.class);
                }
                break;

            case STATIC:

                if (error != null) {

                    add(new WndError(error) {
                        public void onBackPressed() {
                            super.onBackPressed();
                            Game.switchScene(StartScene.class);
                        }
                    });

                    error = null;

                }
                break;
        }
    }

    private void descend() throws Exception {

        Actor.fixTime();

        if (Dungeon.hero == null) {
            Dungeon.init();
            if (noStory) {
                Dungeon.chapters.add(WndStory.ID_SEWERS);
                noStory = false;
            }
        } else {
            Dungeon.saveAll();
        }

        Level level;
        if (Dungeon.depth >= Statistics.deepestFloor) {
            level = Dungeon.newLevel();
        } else {
            Dungeon.depth++;
            level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        }
        Dungeon.switchLevel(level, level.entrance);
    }

    private void fall() throws Exception {

        Actor.fixTime();
        Dungeon.saveAll();

        Level level;

        if (Dungeon.depth <= 25) {

            if (Dungeon.depth >= Statistics.deepestFloor) {
                level = Dungeon.newLevel();
            } else {
                Dungeon.depth++;
                level = Dungeon.loadLevel(Dungeon.hero.heroClass);
            }
        } else {
            // You hear distant a sound of  malicious laughter.
            level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        }

        Dungeon.switchLevel(level, fallIntoPit ? level.pitCell() : level.randomRespawnCell(true, true));
    }

    private void ascend() throws Exception {
        Actor.fixTime();

        Dungeon.saveAll();
        Dungeon.depth--;
        Level level = Dungeon.
                loadLevel(Dungeon.hero.heroClass);
        Dungeon.switchLevel(level, level.exit);
    }

    private void returnTo() throws Exception {

        Actor.fixTime();

        Dungeon.saveAll();
        Dungeon.depth = returnDepth;
        Level level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
    }

    private void restore() throws Exception {

        Actor.fixTime();

        Dungeon.loadGame(StartScene.curClass);
        if (Dungeon.depth == -1) {
            Dungeon.depth = Statistics.deepestFloor;
            Dungeon.switchLevel(Dungeon.loadLevel(StartScene.curClass), -1);
        } else {
            Level level = Dungeon.loadLevel(StartScene.curClass);
            Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(Dungeon.hero.pos) : Dungeon.hero.pos);
        }
    }

//	private void resurrect() throws Exception {
//
//        Actor.fixTime();
//
//        if (Dungeon.bossLevel()) {
//
//            Dungeon.hero.resurrect( Dungeon.depth );
//            Dungeon.depth--;
//            Level level = Dungeon.newLevel();
//            Dungeon.switchLevel( level, level.entrance );
//
//        } else {
//
//            Dungeon.hero.resurrect(-1);
//            Actor.clear();
//            Arrays.fill(Dungeon.visible, false);
//            Dungeon.level.reset();
//            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
//
//        }
//    }

    @Override
    protected void onBackPressed() {
        // Do nothing
    }
}
