package com.lineage.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author YangJunNan
 * @description
 * @date 2021/12/5
 */
public class Similarity {
    static Logger logger = Logger.getLogger(Similarity.class.getName());
    public Map<String, Double> ratingMap = new ConcurrentHashMap<>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        Similarity similarity1 = new Similarity();
        similarity1.ratingMap.put("1", 434d);
        similarity1.ratingMap.put("2", 7d);
        similarity1.ratingMap.put("3", 23d);
        Similarity similarity2 = new Similarity();
        similarity2.ratingMap.put("1", 6d);
        similarity2.ratingMap.put("2", 2d);
        similarity2.ratingMap.put("3", 6d);
        logger.info("" + similarity1.getSimilarity(similarity2));
    }

    public double getSimilarity(Similarity u) {
        double sim = 0d;
        double commonItemsLen = 0;
        double thisSum = 0d;
        double uSum = 0d;
        double thisSumSq = 0d;
        double uSumSq = 0d;
        double pSum = 0d;

        for (String ratingMapIteratorKey : this.ratingMap.keySet()) {
            for (String uRatingMapIteratorKey : u.ratingMap.keySet()) {
                if (ratingMapIteratorKey.equals(uRatingMapIteratorKey)) {
                    double thisGrade = this.ratingMap.get(ratingMapIteratorKey);
                    double uGrade = u.ratingMap.get(uRatingMapIteratorKey);
                    //评分求和
                    //平方和
                    //乘积和
                    thisSum += thisGrade;
                    uSum += uGrade;
                    thisSumSq += Math.pow(thisGrade, 2);
                    uSumSq += Math.pow(uGrade, 2);
                    pSum += thisGrade * uGrade;
                    commonItemsLen++;
                }
            }
        }
        //如果等于零则无相同条目，返回sim=0即可
        if (commonItemsLen > 0) {
            logger.info("commonItemsLen:" + commonItemsLen);
            logger.info("pSum:" + pSum);
            logger.info("thisSum:" + thisSum);
            logger.info("uSum:" + uSum);
            double num = commonItemsLen * pSum - thisSum * uSum;
            double den =
                    Math.sqrt((commonItemsLen * thisSumSq - Math.pow(thisSum, 2)) * (commonItemsLen * uSumSq - Math.pow(uSum, 2)));
            logger.info("" + num + ":" + den);
            sim = (den == 0) ? 1 : num / den;
        }
        //如果等于零则无相同条目，返回sim=0即可
        return sim;
    }
}
