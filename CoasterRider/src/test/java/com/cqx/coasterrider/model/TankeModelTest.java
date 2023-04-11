package com.cqx.coasterrider.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TankeModelTest {

    @Test
    public void move() {
        Random random = new Random(System.currentTimeMillis());
        Map<Integer, Count> sumMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            int move_attach = random.nextInt(3);
            Count sum = sumMap.get(move_attach);
            if (sum == null) {
                sum = new Count();
                sumMap.put(move_attach, sum);
            }
            sum.add();
        }
        System.out.println(sumMap);
    }

    class Count {
        int sum;

        void add() {
            sum++;
        }

        public String toString() {
            return sum + "";
        }
    }
}