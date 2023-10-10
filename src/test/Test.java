package test;

import java.io.File;
import java.io.IOException;

import static util.FileToolUtil.*;

public class Test {
    public static void main(String[] args) throws IOException {
        String file = "OriginalData/dir2/aaa.txt";
        File f = new File(file);
        fileExistEval(f);
    }
}
