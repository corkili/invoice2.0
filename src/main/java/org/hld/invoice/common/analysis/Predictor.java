package org.hld.invoice.common.analysis;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Predictor {
    public Double predict(List<Double> list){

        if (list.size() <= 4) {
            return null;
        }

        BPNet bp = new BPNet(3, 15, 1);

        double[] datus = new double[list.size()];

        for (int i = 0; i < datus.length; i++) {
            datus[i] = list.get(i);
        }

        int n = datus.length - 3;

        double[][] p = new double[n][3];
        double[][] p1 = new double[n][3];

        double[][] t = new double[n][3];
        double[][] t1 = new double[n][1];

        double[] sub1 = new double[n + 3];
        double[] sub2 = new double[n];

        System.arraycopy(datus, 0, sub1, 0, sub1.length);
        System.arraycopy(datus, 3, sub2, 0, sub2.length);

        double minIn = getMin(sub1);
        double minOut = getMin(sub2);
        double maxIn = getMax(sub1);
        double maxOut = getMax(sub2);

        // 数据装填及归一化
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                p[i][j] = datus[i + j];
                p1[i][j] = preprocess(maxIn, minIn, p[i][j]);
            }
            t[i][0] = datus[i + 3];
            t1[i][0] = preprocess(maxOut, minOut, t[i][0]);
//            System.out.println(Arrays.toString(p1[i]) + ": " + Arrays.toString(t1[i]));
        }


        int times = 10000;

        for (int time = 0; time < times; time++) {
            for (int i = 0; i < p1.length; i++) {
                bp.train(p1[i], t1[i]);
            }
            for (int i = p1.length - 1; i >= 0; i--) {
                bp.train(p1[i], t1[i]);
            }
        }

        System.out.println();

/*        //根据训练结果来检验样本数据
        for(int i = 0; i < p1.length; i++){
            double[] result = bp.compute(p1[i]);
            System.out.println(Arrays.toString(p[i]) + ": " + new BigDecimal(revprocess(maxOut, minOut, result[0]))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        }*/

//        System.out.println();

        //根据训练结果来预测一条新数据
        double[] x = new double[3];
        double[] x1 = new double[3];
        System.arraycopy(datus, n, x, 0, 3);
        for (int i = 0; i < x.length; i++)
            x1[i] = preprocess(maxIn, minIn, x[i]);
        double[] result = bp.compute(x1);
        System.out.println(Arrays.toString(x) + ": " + new BigDecimal(revprocess(maxOut, minOut, result[0]))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return new BigDecimal(revprocess(maxOut, minOut, result[0]))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    private double getMin(double[] a) {
        double x = Double.MAX_VALUE;
        for (Double anA : a) {
            if (x > anA)
                x = anA;
        }
        return x;
    }

    private double getMax(double[] a) {
        double x = Double.MIN_VALUE;
        for (Double anA : a) {
            if (x < anA)
                x = anA;
        }
        return x;
    }

    /**归一化处理**/
    private double preprocess(double max, double min, double val){
        /*return 1d / (1d + Math.exp(-val));*/
        if (max == min) {
            return 0;
        } else {
//            return ((val - min) / (max - min)) * 0.8 + 0.1;
            return (val - min) / (max - min);
        }
    }

    /**反归一化处理**/
    private double revprocess(double max, double min, double val){
        /*return -Math.log((1d-val)/val);*/
        if (max == min) {
            return 0;
        } else {
//            return ((val - 0.1) / 0.8) * (max - min) + min;
            return val * (max - min) + min;
        }
    }
}
