package com.fimi.x8sdk.update.fwpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;


public class ParaserTxt {
    public static PkgDetl readTxtFile(String filePath) {
        PkgDetl pkgDtl = new PkgDetl();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader bufferedReader = new BufferedReader(read);
                FwInfo info = null;
                while (true) {
                    String lineTxt = bufferedReader.readLine();
                    if (lineTxt == null) {
                        break;
                    }
                    if (lineTxt.contains("mainver")) {
                        pkgDtl.setMainVer(Short.valueOf(getValue(lineTxt)).shortValue());
                    }
                    if (lineTxt.contains("[FW")) {
                        info = new FwInfo();
                    }
                    if (lineTxt.contains("subver")) {
                        CharSequence temp = getValue(lineTxt);
                        pkgDtl.setSuberVer((short) temp.charAt(0));
                    }
                    if (lineTxt.contains("id")) {
                        pkgDtl.setId1(Integer.valueOf(getValue(lineTxt)).intValue());
                    }
                    if (lineTxt.contains("modelid")) {
                        info.setModelId(Byte.valueOf(getValue(lineTxt)).byteValue());
                    }
                    if (lineTxt.contains("typeid")) {
                        info.setTypeId(Byte.valueOf(getValue(lineTxt)).byteValue());
                    }
                    if (lineTxt.contains("fwtype")) {
                        info.setFwType(Byte.valueOf(getValue(lineTxt)).byteValue());
                    }
                    if (lineTxt.contains("softwarever")) {
                        info.setSoftwareVer(Short.valueOf(getValue(lineTxt)).shortValue());
                    }
                    if (lineTxt.contains("sver")) {
                        CharSequence temp2 = getValue(lineTxt);
                        info.setStepVer((byte) temp2.charAt(0));
                    }
                    if (lineTxt.contains("path")) {
                        info.setSysName(getValue(lineTxt));
                        pkgDtl.getFws().add(info);
                    }
                    System.out.println(lineTxt);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return pkgDtl;
    }

    public static String getValue(String lineText) {
        String text = lineText.trim().substring(lineText.indexOf("=") + 1, lineText.length());
        return text;
    }

    public static void main(String[] argv) {
        PkgDetl pkg = readTxtFile("D:/fm_package_30.ini");
        System.out.println("pkg:" + pkg);
        List<FwInfo> fws = pkg.getFws();
        if (!fws.isEmpty()) {
            for (FwInfo info : fws) {
                System.out.println(info.toString());
            }
        }
    }
}
