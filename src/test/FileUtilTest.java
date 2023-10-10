package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


import static util.FileMainFunc.*;

public class FileUtilTest {
    public static void main(String[] args) throws IOException {
        String srcDir = "D:\\learning_programs\\java_programs\\DataRestore\\OriginalData\\";
        // 源文件的遍历（todo: 后续在这里后面添加筛选文件）
        List<String> filePathSet = fileWalk(srcDir);
        System.out.println(filePathSet);
        // 将源文件整合到一个备份文件中
        String backDir = "D:\\learning_programs\\java_programs\\DataRestore\\BackupData\\";
        int[] srcSize = new int[filePathSet.size()];
        String backFilePath = fileExtract(filePathSet, srcDir, backDir, srcSize);
        System.out.println(srcSize.length);
        System.out.println(backFilePath);
        // 将备份文件恢复到指定目录下
        String resDir = "D:\\learning_programs\\java_programs\\DataRestore\\RestoreData\\";
        fileRestore(backFilePath, resDir);
    }
}
