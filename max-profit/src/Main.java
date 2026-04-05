package org.example;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
            int[] testCases = {7,8,13};
            Profit profit = new Profit();

            for(int n: testCases){
                System.out.println("Max Profit: $" + profit.maxEarnings(n));

                System.out.println("Plans:");
                for (Map<Building, Integer> m : profit.maxEarningsWithPlan(n)) {
                    System.out.println(
                            "T:" + m.get(Building.THEATRE) +
                                    " P:" + m.get(Building.PUB) +
                                    " C:" + m.get(Building.COMMERCIAL)
                    );
                }

            }
    }
}