package com.cmpay.lemon.monitor.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author: zhou_xiong
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcelBySax(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {
            LOGGER.error("excel读取失败,失败信息:", e);
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
        return list;
    }

    public static void doWrite(String filePath, HttpServletResponse res) {
        try {
            setHeader(filePath, res);
            doResWrite(filePath, res);
        } catch (Exception e) {
            LOGGER.error("下载报表失败！失败信息：", e);
            BusinessException.throwBusinessException(MsgEnum.WRITE_FILE_ERROR);
        }
    }

    private static void setHeader(String filePath, HttpServletResponse res) throws IOException{
        res.setContentType("application/octet-stream; charset=utf-8");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        res.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + fileName );
        res.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
    }

    private static void doResWrite(String filePath, HttpServletResponse res) throws IOException {
        ServletOutputStream out = res.getOutputStream();
        ClassPathResource resource = new ClassPathResource(filePath);
        File file = resource.getFile();
        System.err.println(file.getName());
        out.write(org.apache.commons.io.FileUtils.readFileToByteArray(file));
        out.close();
    }
    /**
     * 创建目录
     *
     * @param descDirName
     *            目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            LOGGER.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            LOGGER.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            LOGGER.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }
}
