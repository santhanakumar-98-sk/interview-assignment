package org.example;
import java.util.*;

public class Profit {

    private static final int MAX_BUILD_TIME =
            Arrays.stream(Building.values())
                    .mapToInt(b -> b.time)
                    .max()
                    .orElse(0);

    private static final int MIN_BUILD_TIME =
            Arrays.stream(Building.values())
                    .mapToInt(b -> b.time)
                    .min()
                    .orElse(0);

    private static final int WINDOW_SIZE = MAX_BUILD_TIME + 1;


    private static boolean isBuildable(int totalTime) {
        return MIN_BUILD_TIME > totalTime;
    }

    public long maxEarnings(int totalTime) {

        if(isBuildable(totalTime)){
            return 0;
        }

        long[] dp = new long[WINDOW_SIZE];

        for (int i = 1; i <= totalTime; i++) {
            long max = 0;

            for (Building b : Building.values()) {
                if (i >= b.time) {
                    int rem = i - b.time;
                    long val = dp[rem % WINDOW_SIZE] + (long) rem * b.earnRate;

                    max = Math.max(max, val);
                }
            }

            dp[i % WINDOW_SIZE] = max;
        }
        return  dp[totalTime % WINDOW_SIZE];

    }

    public List<Map<Building, Integer>> maxEarningsWithPlan(int totalTime) {

        if(isBuildable(totalTime)){
            return  new ArrayList<>();
        }

        long[] dp = compute(totalTime);

        List<Map<Building, Integer>> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        EnumMap<Building, Integer> buildingCounts = initMap();

        collect(totalTime, dp, buildingCounts, result, seen);

        return result;
    }

    private void collect(int totalTime, long[] dp, EnumMap<Building, Integer> buildingCounts,
                         List<Map<Building, Integer>> result, Set<String> seen) {

        if (dp[totalTime] == 0) {
            String key = planKey(buildingCounts);

            if (seen.add(key)) {
                result.add(new EnumMap<>(buildingCounts));
            }
            return;
        }

        for (Building b : Building.values()) {

            if (totalTime >= b.time) {

                int rem = totalTime - b.time;
                long val = dp[rem] + (long) rem * b.earnRate;

                if (val == dp[totalTime]) {
                    buildingCounts.put(b, buildingCounts.get(b) + 1);
                    collect(rem, dp, buildingCounts, result, seen);
                    buildingCounts.put(b, buildingCounts.get(b) - 1);
                }
            }
        }
    }

    private EnumMap<Building, Integer> initMap() {
        EnumMap<Building, Integer> map = new EnumMap<>(Building.class);
        for (Building b : Building.values()) {
            map.put(b, 0);
        }
        return map;
    }

    private String planKey(EnumMap<Building, Integer> map) {
        return map.get(Building.THEATRE) + "-" +
                map.get(Building.PUB) + "-" +
                map.get(Building.COMMERCIAL);
    }

    private  static  long[] compute(int  n){

        long[] dp = new long[n+1];

        for (int i = 1; i <= n; i++) {
            long max = 0;

            for (Building b : Building.values()) {
                if (i >= b.time) {
                    int rem = i - b.time;
                    long val = dp[rem] + (long) rem * b.earnRate;

                    max = Math.max(max, val);
                }
            }

            dp[i] = max;
        }
        return dp;

    }
}
