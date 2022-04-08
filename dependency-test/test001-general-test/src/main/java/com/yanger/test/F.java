package com.yanger.test;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @Author yanger
 * @Date 2022/4/5/005 18:41
 */
public class F {

    public static void main(String[] args) {
        File f = new File("C:\\Users\\Administrator\\Desktop\\files\\merge-result.maf");
        File dir = new File("C:\\Users\\Administrator\\Desktop\\files\\files");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            boolean head = false;
            if (dir.isDirectory()) {
                File files[] = dir.listFiles();
                for (File file : files) {
                    if(file.getName().endsWith(".maf.gz")) {
                        continue;
                    }
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        while ((line = br.readLine()) != null) {
                            if(!head && line.startsWith("Hugo_Symbol")) {
                                bw.write(line);
                                bw.newLine();
                                head = true;
                            }
                            if(StringUtils.isNotBlank(line) && !line.startsWith("#") && !line.startsWith("Hugo_Symbol")) {
                                bw.write(line);
                                bw.newLine();
                            }
                            System.out.println(line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            bw.close();
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
