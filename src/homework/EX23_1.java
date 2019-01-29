package homework;

import java.io.*;
import java.util.Arrays;

public class EX23_1 {

    static void read(File file) {
        int charCount = 0, numCount = 0, spaceCount = 0;
        if (!file.exists())
            return;
        if (file.isDirectory())
            return;
        FileInputStream fileInputStream;
        byte[] bytes = new byte[1024];
        int ch;
        try {
            fileInputStream = new FileInputStream(file);
            while ((ch = fileInputStream.read(bytes)) != -1) {
                for (int i = 0; i < ch; i++) {
                    if (bytes[i] >= 48 && bytes[i] <= 57)
                        numCount++;
                    if (bytes[i] >= 'A' && bytes[i] < 'z')
                        charCount++;
                    if (bytes[i] == 32)
                        spaceCount++;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("空格：" + spaceCount + "   字母：" + charCount + "    数字：" + numCount);

    }

    static void read2(File file) throws IOException {
        char[] chars = new char[1024];
        if (!file.exists())
            return;
        if (file.isDirectory())
            return;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String s = "";
        StringBuilder sb = new StringBuilder();
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }
        bufferedReader.close();
        s = sb.toString().replaceAll("[^A-Za-z]", "");
        char[] chars1 = s.toCharArray();
        Arrays.sort(chars1);
        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        pw.println(chars1);
        pw.close();


    }

    public static void main(String[] args) {
        String basePath = System.getProperties().getProperty("user.dir");
        //read(new File("C:\\Users\\Y.Y-PC.000\\Desktop\\a.txt"));
        try {
            read2(new File("C:\\Users\\Y.Y-PC.000\\Desktop\\a.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
