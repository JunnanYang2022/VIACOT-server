package com.lineage.utils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author YangJunNan
 * @description
 * @date 2021/11/23
 */
public class MathUtils {

    private double calculatePearson(Map<BigInteger, Double> mapX, Map<BigInteger, Double> mapY) {
        // (sum(X*Y) - (sum(X)*sum(Y)/N))/sqr((sum(pow(X))-(pow(sum(X))/N)) * ((sum(pow(Y))-(pow(sum(Y))/N))))
        double sumxy = 0d;
        double sumX = 0d;
        double sumY = 0d;
        double sumPowX = 0d;
        double sumPowY = 0d;
        Set<BigInteger> setItem = new HashSet<>();
        for (Map.Entry<BigInteger, Double> entry : mapX.entrySet()) {
            setItem.add(entry.getKey());
        }
        for (Map.Entry<BigInteger, Double> entry : mapY.entrySet()) {
            setItem.add(entry.getKey());
        }
        for (BigInteger bookId : setItem) {
            Double x = mapX.get(bookId);
            if (x == null) {
                x = 0d;
            }
            Double y = mapY.get(bookId);
            if (y == null) {
                y = 0d;
            }
            sumxy += x * y;
            sumX += x;
            sumY += y;
            sumPowX += Math.pow(x, 2);
            sumPowY += Math.pow(y, 2);
        }
        int n = setItem.size();
        double pearson = (sumxy - sumX * sumY / n) / Math.sqrt((sumPowX - Math.pow(sumX, 2) / n) * (sumPowY - Math.pow(sumY, 2) / n));
        return pearson;
    }
}
