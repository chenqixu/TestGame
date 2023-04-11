package com.cqx.coasterrider.util;

import com.cqx.coasterrider.model.EnumDirection;
import com.cqx.coasterrider.model.PieceType;
import com.cqx.coasterrider.model.TankeModel;
import com.cqx.coasterrider.model.TkContainer;
import com.cqx.common.utils.excel.ExcelSheetList;
import com.cqx.common.utils.excel.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 获取地图矩阵数据
 *
 * @author chenqixu
 */
public class BuildMapUtil {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapUtil.class);
    private String sucai_path = "d:\\tmp\\data\\show\\";
    private String brick1_path = sucai_path + "onebrick.jpg";
    private String brick2_path = sucai_path + "brick2.jpg";
    private String home_path = sucai_path + "home.jpg";
    private String tanke_t1_path = sucai_path + "tanke-t1-%s.jpg";
    private String tanke_d1_path = sucai_path + "tanke-d1-%s.jpg";
    private ExcelUtils excelUtils;

    public BuildMapUtil() {
        excelUtils = new ExcelUtils(true);
    }

    public TkContainer buildTanke(String fileName, int width, int height) throws IOException {
        TkContainer tkContainer = new TkContainer(width, height);
        List<ExcelSheetList> excelSheetLists = excelUtils.readExcel(fileName);
        for (ExcelSheetList sheet : excelSheetLists) {
            if (sheet.getSheetName().equals("Sheet1")) {
                List<List<String>> sheetList = sheet.getSheetList();
                int _width = sheetList.size() - 1;
                int _height = sheetList.get(0).size();
                for (int i = 0; i < _width; i++) {
                    List<String> row = sheetList.get(i);
                    for (int j = 0; j < _height; j++) {
                        String val = row.get(j) == null ? "" : row.get(j);
                        switch (val) {
                            case "b1":
                                tkContainer.addBrick(j, i, new TankeModel(PieceType.Brick, "b1-" + i + j, brick1_path));
                                break;
                            case "b2":
                                tkContainer.addBrick(j, i, new TankeModel(PieceType.Brick, "b2-" + i + j, brick2_path, 2));
                                break;
                            case "h1":
                                tkContainer.addHome(j, i, new TankeModel(PieceType.Home, "h1-" + i + j, home_path));
                                break;
                            case "t1":
                                tkContainer.addPlayer(j, i, new TankeModel(PieceType.Tanke, "t1-" + i + j, tanke_t1_path), EnumDirection.UP);
                                break;
                            case "d1":// 普通型
                                tkContainer.addAI(j, i, new TankeModel(PieceType.Tanke, "d1-" + i + j, tanke_d1_path), EnumDirection.DOWN);
                                break;
                            case "d2":// 移动速度快
                                break;
                            case "d3":// 生命多
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return tkContainer;
    }
}
