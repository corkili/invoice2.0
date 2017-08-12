package org.hld.invoice.common.analysis;

/**
 * Created by 李浩然 on 2017/6/10.
 */
public class IncomeAndOutcomeAnalysisModel implements Comparable<IncomeAndOutcomeAnalysisModel> {
    private String date;
    private double incomes;
    private double outcomes;

    public IncomeAndOutcomeAnalysisModel(String date, double incomes, double outcomes) {
        this.date = date;
        this.incomes = incomes;
        this.outcomes = outcomes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getIncomes() {
        return incomes;
    }

    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    public double getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(double outcomes) {
        this.outcomes = outcomes;
    }

    @Override
    public int compareTo(IncomeAndOutcomeAnalysisModel o) {
        return date.compareTo(o.getDate());
    }
}
