package de.ur.mi.reactiontest.Helper;

import de.ur.mi.reactiontest.Config.Config;
import de.ur.mi.reactiontest.Enums.PanelType;

import java.util.ArrayList;

public class TestStatistic {
    private PanelType testType;
    private ArrayList<Long> allTimes = new ArrayList<>();

    public TestStatistic(PanelType testType){
        this.testType = testType;
    }

    public void setNextTime(long timeInMillis) {
        allTimes.add(timeInMillis);
    }

    public void reset() {
        allTimes.clear();
    }

    public boolean hasResult() {
        return allTimes.size() >= Config.PASSES_PER_TEST;
    }

    public int getCurrentPass() {
        return allTimes.size();
    }

    public PanelType getTestType() {
        return testType;
    }

    public long getAverageTime() {
        long total = 0;
        for (long l : allTimes) {
            total += l;
        }
        return total / allTimes.size();
    }

    public long getBestTime() {
        long best = -1;
        for (long l : allTimes) {
            if (best == -1 || l < best) {
                best = l;
            }
        }
        return best;
    }

    public long getWorstTime() {
        long worst = -1;
        for (long l : allTimes) {
            if (l > worst) {
                worst = l;
            }
        }
        return worst;
    }

    public TestStatistic copyOf() {
        TestStatistic r = new TestStatistic(testType);
        for(Long l : allTimes){
            r.setNextTime(l);
        }
        return r;
    }
}
