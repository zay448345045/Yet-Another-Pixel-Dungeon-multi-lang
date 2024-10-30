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
package com.consideredhamster.yetanotherpixeldungeon;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SystemTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public enum Rankings {

    INSTANCE;

    public static final int TABLE_SIZE = 6;

    public static final String RANKINGS_FILE = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.rankings_file");
    public static final String DETAILS_FILE = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.details_file");

    public ArrayList<Record> records;
    //	public int loadedDiff;
    public int lastRecord;

    public int totalNumber;
    public int wonNumber;

    public void submit(boolean win) {

        load(Dungeon.difficulty);

        Record rec = new Record();

        rec.info = Dungeon.resultDescription;
        rec.win = win;
        rec.heroClass = Dungeon.hero.heroClass;
        rec.armorTier = Dungeon.hero.appearance();
        rec.score = score(win);
        rec.version = Game.version;
        rec.level = Dungeon.hero.lvl;
        rec.depth = Statistics.deepestFloor;
        rec.diff = Dungeon.difficulty;

        String gameFile = Utils.format(DETAILS_FILE, SystemTime.now);
        try {
            Dungeon.saveGame(gameFile);
            rec.gameFile = gameFile;
        } catch (IOException e) {
            rec.gameFile = "";
        }

        records.add(rec);

        Collections.sort(records, scoreComparator);

        lastRecord = records.indexOf(rec);
        int size = records.size();
        if (size > TABLE_SIZE) {

            Record removedGame;
            if (lastRecord == size - 1) {
                removedGame = records.remove(size - 2);
                lastRecord--;
            } else {
                removedGame = records.remove(size - 1);
            }

            if (removedGame != null && removedGame.gameFile.length() > 0) {
                Game.instance.deleteFile(removedGame.gameFile);
            }
        }

        totalNumber++;
        if (win) {
            wonNumber++;
        }

//		Badges.validateGamesPlayed();
        YetAnotherPixelDungeon.lastDifficulty(Dungeon.difficulty);

        save(Dungeon.difficulty);
    }

    private int score(boolean win) {
        return (Statistics.goldCollected + Statistics.enemiesSlain * 5 + Dungeon.hero.lvl * Statistics.deepestFloor * 100) * (win ? 2 : 1) * (Dungeon.difficulty + 1);
    }

    private static final String RECORDS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.records");
    private static final String LATEST = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.latest");
    private static final String TOTAL = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.total");
    private static final String WON = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.won");

    public void save(int difficulty) {
        Bundle bundle = new Bundle();
        bundle.put(RECORDS, records);
        bundle.put(LATEST, lastRecord);
        bundle.put(TOTAL, totalNumber);
        bundle.put(WON, wonNumber);

        String rankingsFile = Utils.format(RANKINGS_FILE, difficulty);

        try {
            OutputStream output = Game.instance.openFileOutput(rankingsFile, Game.MODE_PRIVATE);
            Bundle.write(bundle, output);
            output.close();
        } catch (Exception e) {
        }
    }

    public void load(int difficulty) {

//		if ( records != null && loadedDiff == difficulty ) {
//			return;
//		}

//        loadedDiff = difficulty;
        records = new ArrayList<>();

        String rankingsFile = Utils.format(RANKINGS_FILE, difficulty);

        try {
            InputStream input = Game.instance.openFileInput(rankingsFile);
            Bundle bundle = Bundle.read(input);
            input.close();

            for (Bundlable record : bundle.getCollection(RECORDS)) {
                records.add((Record) record);
            }
            lastRecord = bundle.getInt(LATEST);

            totalNumber = bundle.getInt(TOTAL);
            if (totalNumber == 0) {
                totalNumber = records.size();
            }

            wonNumber = bundle.getInt(WON);
            if (wonNumber == 0) {
                for (Record rec : records) {
                    if (rec.win) {
                        wonNumber++;
                    }
                }
            }

        } catch (Exception e) {
            totalNumber = 0;
            wonNumber = 0;
        }
    }

    public static class Record implements Bundlable {

        private static final String REASON = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.reason");
        private static final String WIN = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.win");
        private static final String SCORE = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.score");
        private static final String TIER = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.tier");
        private static final String GAME = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.game");
        private static final String VERSION = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.version");
        private static final String LEVEL = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.level");
        private static final String DEPTH = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.depth");
        private static final String DIFF = Ml.g("com.consideredhamster.yetanotherpixeldungeon.record.diff");

        public String info;
        public String version;
        public boolean win;

        public HeroClass heroClass;
        public int armorTier;

        public int score;
        public int level;
        public int depth;

        public int diff;

        public String gameFile;

        @Override
        public void restoreFromBundle(Bundle bundle) {

            version = bundle.getString(VERSION);
            info = bundle.getString(REASON);
            win = bundle.getBoolean(WIN);
            score = bundle.getInt(SCORE);
            level = bundle.getInt(LEVEL);
            depth = bundle.getInt(DEPTH);
            diff = bundle.getInt(DIFF);

            heroClass = HeroClass.restoreInBundle(bundle);
            armorTier = bundle.getInt(TIER);

            gameFile = bundle.getString(GAME);
        }

        @Override
        public void storeInBundle(Bundle bundle) {

            bundle.put(VERSION, version);
            bundle.put(REASON, info);
            bundle.put(WIN, win);
            bundle.put(SCORE, score);
            bundle.put(LEVEL, level);
            bundle.put(DEPTH, depth);
            bundle.put(DIFF, diff);

            heroClass.storeInBundle(bundle);
            bundle.put(TIER, armorTier);

            bundle.put(GAME, gameFile);
        }
    }

    private static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
        @Override
        public int compare(Record lhs, Record rhs) {
            return rhs != null && lhs != null ? (int) Math.signum(rhs.score - lhs.score) : 0;
        }
    };
}
