package util;

import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static util.FileToolUtil.*;

public class FileMainFunc {
    // 工具类，禁止创建对象
    private FileMainFunc() {
    }

    public static List<String> fileWalk(String srcDir){
        List<String> filePathSet = new ArrayList<String>();
        File src = new File(srcDir);
        // 判断源目录是否存在
        if (!src.isDirectory())
            return filePathSet;

        fileWalkLoop(srcDir, filePathSet);
        for (int i = 0; i < filePathSet.size(); i++) {
            String filePath = filePathSet.get(i).replace(srcDir, "");
            filePathSet.set(i, filePath);
        }
        return filePathSet;
    }

    // 根据源目录下文件的路径，将所有文件提取到备份文件中，并获取其各个文件的大小
    public static String fileExtract(List<String> filePathSet, String srcDir, String backDir, int[] srcSize) {
        String backFilePath = "";
        File back = new File(backDir);
        // 判断目的目录是否存在，不存在且创建失败则直接返回
        if(!dirExistEval(back))
            return backFilePath;

        InputStream is = null;
        OutputStream os = null;
        int fileNum = filePathSet.size();
        HashMap<String, String> metaMap = new HashMap<String, String>();
        // 根据当前时间，为备份文件命名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Calendar calendar = Calendar.getInstance();
        backFilePath = backDir + df.format(calendar.getTime());
        // 缓存容器
        try {
            os = new FileOutputStream(backFilePath);
            // 遍历所有源文件，并复制到备份文件中
            for (int i = 0; i < fileNum; i++) {
                String inFilePath = filePathSet.get(i);
                is = new FileInputStream(srcDir+inFilePath);
                srcSize[i] = fileCopy(is, os);
                metaMap.put(inFilePath, ""+srcSize[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeJson(backFilePath+".json", metaMap);


        return backFilePath;
    }

    // 根据备份文件和json文件，将文件还原到指定路径下
    public static void fileRestore(String backFilePath, String resDir) throws IOException {
        String backJsonPath = backFilePath+".json";
        File backFile = new File(backFilePath);
        FileInputStream is = new FileInputStream(backFile);
        JSONObject metaJson = readJson(backJsonPath);
        int fileLen = -1;
        for(String key : metaJson.keySet()){
            fileLen = metaJson.getInt(key);
            fileRestoreSingle(is, resDir+key, fileLen);
        }
    }
}
