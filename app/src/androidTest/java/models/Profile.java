package models;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    public String accountId;

    public   int platformId;

    public   String platformName;

    public   String platformNameLong;

    public   String epicUserHandle;

    public Stats stats;

    public List<LifeTimeStat> lifeTimeStats;

    public List<RecentMatche> recentMatches;
}
