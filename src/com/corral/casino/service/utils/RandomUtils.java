package com.corral.casino.service.utils;

import java.util.Random;

public class RandomUtils {

    public static int generateInitialSalary () {
        Random random =  new Random();
        int ran1 = random.nextInt(10) +1;
        int ran2 = random.nextInt(10) +1;
        int ran3 = random.nextInt(10) +1;

        int ranFin = ran1 * ran2 * ran3;
        return ranFin < 100 ? 100 : ranFin;
    }

}
