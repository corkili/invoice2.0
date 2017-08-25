package org.hld.invoice.common.analysis;

import org.hld.invoice.common.model.Result;
import org.hld.invoice.entity.Invoice;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Analyzer {
    private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy年");
    private static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy年MM月");
    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy年MM月dd日");

    public static Result analyzeForChart(String pattern, List<Invoice> incomeInvoices, List<Invoice> outcomeInvoices) {
        boolean successful = false;
        String message;
        List<String> dates = new ArrayList<>();
        List<Double> incomes = new ArrayList<>();
        List<Double> outcomes = new ArrayList<>();
        if (incomeInvoices.size() + outcomeInvoices.size() <= 0) {
            message = "没有发票数据，无法分析！";
        } else {
            AnalysisHelper analysisHelper = new AnalysisHelper(pattern, incomeInvoices, outcomeInvoices);
            List<IncomeAndOutcomeAnalysisModel> incomeAndOutcomeAnalysisResult = analysisHelper.getIncomeAndOutcomeAnalysisResult();
            for (IncomeAndOutcomeAnalysisModel m : incomeAndOutcomeAnalysisResult) {
                dates.add(m.getDate());
                incomes.add(new BigDecimal(m.getIncomes()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                outcomes.add(new BigDecimal(m.getOutcomes()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            successful = true;
            message = "分析成功！";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("dates", dates)
                .add("incomes", incomes)
                .add("outcomes", outcomes);
        return result;
    }

    public static Result analyzeForReport(String pattern, List<Invoice> incomeInvoices,
                                          List<Invoice> outcomeInvoices, Date startDate, Date endDate) {
        boolean successful = false;
        String message;
        List<String> dates = new ArrayList<>();
        List<Double> incomes = new ArrayList<>();
        List<Double> outcomes = new ArrayList<>();
        List<Double> balances = new ArrayList<>();
        List<Double> incomeProductTotals = new ArrayList<>();
        List<Double> outcomeProductTotals = new ArrayList<>();
        List<String> incomeNames = new ArrayList<>();
        List<String> outcomeNames = new ArrayList<>();
        List<List<Double>> incomeAmounts = new ArrayList<>();
        List<List<Double>> outcomeAmounts = new ArrayList<>();
        String incomeComment = "";
        String outcomeComment = "";
        StringBuilder compareComment = new StringBuilder();
        // 预测数据
        String preDate = "";
        double preIncome = 0, preOutcome = 0;

        if (incomeInvoices.size() + outcomeInvoices.size() <= 0) {
            message = "没有发票数据，无法分析！";
        } else {
            AnalysisHelper analysisHelper = new AnalysisHelper(pattern, incomeInvoices, outcomeInvoices);
            // 装载进销项对比分析数据
            List<IncomeAndOutcomeAnalysisModel> incomeAndOutcomeAnalysisResult = analysisHelper.getIncomeAndOutcomeAnalysisResult();
            for (IncomeAndOutcomeAnalysisModel m : incomeAndOutcomeAnalysisResult) {
                dates.add(m.getDate());
                incomes.add(new BigDecimal(m.getIncomes()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                outcomes.add(new BigDecimal(m.getOutcomes()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                balances.add(new BigDecimal(m.getOutcomes() - m.getIncomes()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            // 进销项产品数据分析
            List<List<ProductAnalysisModel>> incomeAndOutcomeProductAnalysis = analysisHelper.getProductAnalysisResult();
            List<ProductAnalysisModel> incomeProducts = incomeAndOutcomeProductAnalysis.get(0);
            List<ProductAnalysisModel> outcomeProducts = incomeAndOutcomeProductAnalysis.get(1);

            // 进项产品与销项产品名称装载
            incomeNames.addAll(incomeProducts.get(0).getNames());
            outcomeNames.addAll(outcomeProducts.get(0).getNames());

            // 进项产品与销项产品金额数据加载
            for (ProductAnalysisModel incomeProduct : incomeProducts) {
                incomeAmounts.add(incomeProduct.getAmounts());
            }
            for (ProductAnalysisModel outcomeProduct : outcomeProducts) {
                outcomeAmounts.add(outcomeProduct.getAmounts());
            }

            // 进项数据，年月日求和
            double sum = 0.0;
            for (int i = 0; i < incomeNames.size(); i++) {
                sum = 0.0;
                for (int j = 0; j < incomeAmounts.size(); j++) {
                    sum += incomeAmounts.get(j).get(i);
                }
                incomeProductTotals.add(new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            // 进项数据总和
            sum = 0.0;
            for (Double income : incomes) {
                sum += income;
            }
            incomeProductTotals.add(new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            // 销项数据, 年月总和
            for (int i = 0; i < outcomeNames.size(); i++) {
                sum = 0.0;
                for (List<Double> amounts : outcomeAmounts) {
                    sum += amounts.get(i);
                }
                outcomeProductTotals.add(new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            sum = 0.0;
            for (Double outcome : outcomes) {
                sum += outcome;
            }
            outcomeProductTotals.add(new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            // 总进销项差额
            balances.add(new BigDecimal(outcomeProductTotals.get(outcomeProductTotals.size() - 1)
                    - incomeProductTotals.get(incomeProductTotals.size() - 1))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            // 日期
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Calendar calendar = Calendar.getInstance();
            try {
                startDate = new Date(dateFormat.parse(dates.get(0)).getTime());
                endDate = new Date(dateFormat.parse(dates.get(dates.size() - 1)).getTime());
            } catch (ParseException ignored) {

            }
            calendar.setTime(endDate);
            String dateString = "";
            if (pattern.contains("dd")) {   // 日度
                dateString = dayFormat.format(startDate) + "至" +
                        dayFormat.format(endDate);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                preDate = dateFormat.format(calendar.getTime());
            } else if (pattern.contains("MM")) {    // 月度
                dateString = monthFormat.format(startDate) + "至" +
                        monthFormat.format(endDate);
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                preDate = dateFormat.format(calendar.getTime());
            } else {    // 年度
                dateString = yearFormat.format(startDate) + "至" +
                        yearFormat.format(endDate);
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                preDate = dateFormat.format(calendar.getTime());
            }

            if (dates.size() <= 4) {
                preDate = "";
            } else {
                preDate += "（预测）";
                preIncome = new Predictor().predict(incomes);
                preOutcome = new Predictor().predict(outcomes);
            }

            // 进、销项数据分析
            incomeComment = compareIncomeOrOutcome(incomeAmounts, incomeProductTotals, dateString, "进项", incomeNames);
            outcomeComment = compareIncomeOrOutcome(outcomeAmounts, outcomeProductTotals, dateString, "销项", outcomeNames);

            compareComment.append("进销项对比分析：由上述的数据可知，在")
                    .append(dateString).append("，企业的总进项额为")
                    .append(incomeProductTotals.get(incomeProductTotals.size() - 1))
                    .append("元，").append("总销项额为")
                    .append(outcomeProductTotals.get(outcomeProductTotals.size() - 1))
                    .append("元，").append("进、销项差值为：")
                    .append(Math.abs(balances.get(balances.size() - 1)))
                    .append("元。");
            compareComment.append("在").append(dateString);
            if (balances.get(balances.size() - 1) > 0) {
                compareComment.append("，企业总体运营情况良好！");
            } else if (balances.get(balances.size() - 1) < 0) {
                compareComment.append("，企业总体运营情况不好！");
            } else {
                compareComment.append("，企业总体运营情况稳定！");
            }
            successful = true;
            message = "分析成功！";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("balances", balances)
                .add("income_product_totals", incomeProductTotals)
                .add("outcome_product_totals", outcomeProductTotals)
                .add("income_names", incomeNames)
                .add("outcome_names", outcomeNames)
                .add("income_amounts", incomeAmounts)
                .add("outcome_amounts",outcomeAmounts)
                .add("dates", dates)
                .add("incomes", incomes)
                .add("outcomes", outcomes)
                .add("income_comments", incomeComment)
                .add("outcome_comments", outcomeComment)
                .add("compare_comments", compareComment.toString())
                .add("pre_date", preDate)
                .add("pre_income", preIncome)
                .add("pre_outcome", preOutcome)
                .add("pre_balance", new BigDecimal(preOutcome - preIncome)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return result;
    }

    private static String compareIncomeOrOutcome(List<List<Double>> amounts, List<Double> productTotals,
                                          String dateString, String type, List<String> names) {
        StringBuilder comments = new StringBuilder();
        List<String> Ups = new ArrayList<>();
        List<String> Downs = new ArrayList<>();
        List<String> Holds = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            double diff = amounts.get(amounts.size() - 1).get(i) - amounts.get(0).get(i);
            if (diff > 0) {
                Ups.add(names.get(i));
            } else if (diff < 0) {
                Downs.add(names.get(i));
            } else {
                Holds.add(names.get(i));
            }
        }
        comments.append("由以上数据可以看出，");
        if (Ups.size() > 0) {
            comments.append(Ups.get(0));
            for (int i = 1; i < Ups.size(); i++) {
                comments.append("、").append(Ups.get(i));
            }
            if (Ups.size() > 1) {
                comments.append("等");
            }
            comments.append("产品，在").append(dateString).append("时间段内总体呈现上升趋势");
        }
        if (Downs.size() > 0) {
            comments.append("；").append(Downs.get(0));
            for (int i = 1; i < Downs.size(); i++) {
                comments.append("、").append(Downs.get(i));
            }
            if (Downs.size() > 1) {
                comments.append("等");
            }
            comments.append("产品，在").append(dateString).append("时间段内总体呈现下降趋势");
        }
        if (Holds.size() > 0) {
            comments.append("；").append(Holds.get(0));
            for (int i = 1; i < Holds.size(); i++) {
                comments.append("、").append(Holds.get(i));
            }
            if (Holds.size() > 1) {
                comments.append("等");
            }
            comments.append("产品，在").append(dateString).append("时间段内总体呈现平滑趋势");
        }
        comments.append("。\n");
        comments.append("企业在").append(dateString).append("时间段内，总计").append(type)
                .append(productTotals.get(productTotals.size() - 1)).append("元，总体在")
                .append(dateString).append("时间段内呈现");
        if (productTotals.get(productTotals.size() - 1) > productTotals.get(0)) {
            comments.append("上升");
        } else if (productTotals.get(productTotals.size() - 1) < productTotals.get(0)) {
            comments.append("下降");
        } else {
            comments.append("平滑");
        }
        comments.append("趋势。");
        return comments.toString();
    }
}
