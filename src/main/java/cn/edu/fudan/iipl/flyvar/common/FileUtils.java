package cn.edu.fudan.iipl.flyvar.common;

public class FileUtils {

    /**
     * 获取项目根路径
     * @return String形式的项目根路径
     */
    public static String getFlyvarRoot() {
        return System.getProperty("flyvar.root");
    }
}
