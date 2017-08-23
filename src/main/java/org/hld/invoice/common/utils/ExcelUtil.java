package org.hld.invoice.common.utils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hld.invoice.entity.Invoice;
import org.hld.invoice.entity.InvoiceDetail;

/**
 * 读取Excel
 *
 * @author zengwendong
 */
public class ExcelUtil {
    private Logger logger = Logger.getLogger(ExcelUtil.class);
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat decimalFormat = new DecimalFormat("#");
    public ExcelUtil(String filepath) {
        if(filepath==null){
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);
            if(".xls".equals(ext)){
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(ext)){
                wb = new XSSFWorkbook(is);
            }else{
                wb=null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @author zengwendong
     */
    public String[] readExcelTitle() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = row.getCell(i).getCellFormula();
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     *
     * @return Map 包含单元格数据内容的Map对象
     * @author zengwendong
     */
    public List<Invoice> getInvoicesFromExcel(){
        if(wb == null){
            return null;
        }
        List<Invoice> invoices = new ArrayList<>();  //

        Map<String, Invoice> invoiceMap = new HashMap<>();

        // 解析发票数据
        sheet = wb.getSheetAt(0);
        if (sheet == null) {
            return invoices;
        }
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        logger.info(rowNum);
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        logger.info(colNum);
        if (colNum != 11) {
            return invoices;
        }
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            Invoice invoice = new Invoice();
            Object object;
            int j = 0;
            // 0:发票ID
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setInvoiceId((String)object);
            } else {
                invoice.setInvoiceId(decimalFormat.format(object));
            }
            // 1:发票Code
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setInvoiceCode((String)object);
            } else {
                invoice.setInvoiceCode(decimalFormat.format(object));
            }
            // 2:发票日期
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Date) {
                invoice.setInvoiceDate(new java.sql.Date(((Date)object).getTime()));
            } else if(object instanceof String) {
                try {
                    invoice.setInvoiceDate(new java.sql.Date((dateFormat.parse((String)object)).getTime()));
                } catch (ParseException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 3:购贷方名称
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setBuyerName((String)object);
            } else {
                invoice.setBuyerName(String.valueOf(object));
            }
            // 4:购贷方ID
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setBuyerId((String)object);
            } else {
                invoice.setBuyerId(String.valueOf(object));
            }
            // 5:销贷方名称
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setSellerName((String)object);
            } else {
                invoice.setSellerName(String.valueOf(object));
            }
            // 6:销贷方ID
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                invoice.setSellerId((String)object);
            } else {
                invoice.setSellerId(String.valueOf(object));
            }
            // 7:总金额
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                invoice.setTotalAmount((double)object);
            } else if (object instanceof String){
                try {
                    invoice.setTotalAmount(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 8:总税额
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                invoice.setTotalTax((double)object);
            } else if (object instanceof String){
                try {
                    invoice.setTotalTax(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 9:税价合计
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                invoice.setTotal((double)object);
            } else if (object instanceof String){
                try {
                    invoice.setTotal(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 10:备注
            if ((object = getCellFormatValue(row.getCell(j))) instanceof String) {
                invoice.setRemark((String)object);
            } else {
                invoice.setRemark(decimalFormat.format(object));
            }
            invoice.setDetails(new ArrayList<>());
            invoiceMap.put(invoice.getInvoiceId() + "_" + invoice.getInvoiceCode(), invoice);
        }

        // 解析发票明细数据
        sheet = wb.getSheetAt(1);
        if (sheet == null) {
            return null;
        }
        // 得到总行数
        rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        colNum = row.getPhysicalNumberOfCells();
        if (colNum != 10) {
            return null;
        }
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            InvoiceDetail detail = new InvoiceDetail();
            Object object;
            int j = 0;
            // 0:发票ID
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                detail.setInvoiceId((String) object);
            } else {
                detail.setInvoiceId(decimalFormat.format(object));
            }
            // 1:发票代码
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                detail.setInvoiceCode((String) object);
            } else {
                detail.setInvoiceCode(decimalFormat.format(object));
            }
            // 2:产品名称
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                detail.setDetailName((String) object);
            } else {
                detail.setDetailName(String.valueOf(object));
            }
            // 3:规格型号
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                detail.setSpecification((String) object);
            } else {
                detail.setSpecification(decimalFormat.format(object));
            }
            // 4:单位
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof String) {
                detail.setUnitName((String) object);
            } else {
                detail.setSpecification(String.valueOf(object));
            }
            // 5:数量
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                detail.setQuantity((int)((double)object));
            } else if (object instanceof String){
                try {
                    detail.setQuantity((int)Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 6:单价
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                detail.setUnitPrice((double) object);
            } else if (object instanceof String){
                try {
                    detail.setUnitPrice(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 7:金额
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                detail.setAmount((double) object);
            } else if (object instanceof String){
                try {
                    detail.setAmount(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 8:税率
            if ((object = getCellFormatValue(row.getCell(j++))) instanceof Double) {
                detail.setTaxRate((double) object);
            } else if (object instanceof String){
                try {
                    detail.setTaxRate(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            // 9:税额
            if ((object = getCellFormatValue(row.getCell(j))) instanceof Double) {
                detail.setTaxSum((double) object);
            } else if (object instanceof String){
                try {
                    detail.setTaxSum(Double.parseDouble((String)object));
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                continue;
            }
            try {
                invoiceMap.get(detail.getInvoiceId() + "_" + detail.getInvoiceCode()).getDetails().add(detail);
            } catch (NullPointerException e) {
                logger.info("error: " + detail.getInvoiceId() + "_" + detail.getInvoiceCode());
            }
        }
        invoices.addAll(invoiceMap.values());
        return invoices;
    }

    /**
     *
     * 根据Cell类型设置数据
     *
     * @param cell 单元格
     * @return 单元格数据的类型
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:// 如果当前Cell的Type为NUMERIC
                case FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Date格式
                        // date格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // date格式是不带带时分秒的：2013-7-10
                        cellvalue = cell.getDateCellValue();
                    } else {// 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = cell.getNumericCellValue();
                    }
                    break;
                }
                case STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public static void main(String[] args) {
        try {
            String filepath = "C:\\Users\\李浩然\\Desktop\\　\\project\\中软杯\\test data\\发票数据.xlsx";
            ExcelUtil excelReader = new ExcelUtil(filepath);

            // 对读取Excel表格内容测试
            List<Invoice> invoices = excelReader.getInvoicesFromExcel();
            System.out.println("获得Excel表格的内容:");
            System.out.println(invoices.size());
            int sum = 0;
            for (Invoice invoice : invoices) {
                System.out.println(invoice);
                for (InvoiceDetail detail : invoice.getDetails()) {
                    System.out.println(detail);
                }
                sum += invoice.getDetails().size();
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
