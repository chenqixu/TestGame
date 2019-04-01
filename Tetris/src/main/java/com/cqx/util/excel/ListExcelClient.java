package com.cqx.util.excel;

import java.io.IOException;
import java.util.List;

public class ListExcelClient {

    /**
     * 删表语句
     */
    public static String getValue(String path) {
        StringBuffer sb = new StringBuffer("");
        List<ExcelSheetList> list = null;
        ExcelUtils eu = new ExcelUtils();
        int allsize = 13;
        try {
            list = eu.readExcel(path);
            if (list != null) {
                // 循环sheet
                for (int i = 1; i < list.size(); i++) {
                    // 循环sheet每一行第一列
                    for (int j = 0; j < list.get(i).getSheetList().size(); j++) {
                        if (list.get(i).getSheetList().get(j).size() <= 0) {
                            continue;
                        }
//						System.out.println();
                        for (int x = 0; x < list.get(i).getSheetList().get(j).size(); x++) {
//							System.out.print(list.get(i).getSheetList().get(j).get(x)+" ");
                            if (list.get(i).getSheetList().get(j).get(x).length() > 0) System.out.println(allsize);
                            allsize++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String path = "d:/Document/Workspaces/MyEclipse 10/Tetris/gameover.xlsx";
        System.out.println(ListExcelClient.getValue(path));
    }
}
