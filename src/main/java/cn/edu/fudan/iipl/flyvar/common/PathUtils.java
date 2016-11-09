package cn.edu.fudan.iipl.flyvar.common;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathUtils {

    @Value("${file.uploadFilesPath}")
    private String uploadFilesPath;

    @Value("${file.annotationFilesPath}")
    private String annotationFilesPath;

    @Value("${file.snpSamplesPath}")
    private String snpSamplesPath;

    @Value("${file.scriptFilesPath}")
    private String scriptFilesPath;

    /**
     * 获取项目根路径
     * @return String形式的项目根路径
     */
    public String getFlyvarRoot() {
        return System.getProperty("flyvar.root");
    }

    public String getUploadFilesPath() {
        return uploadFilesPath;
    }

    public void setUploadFilesPath(String uploadFilesPath) {
        this.uploadFilesPath = uploadFilesPath;
    }

    public String getAnnotationFilesPath() {
        return annotationFilesPath;
    }

    public void setAnnotationFilesPath(String annotationFilesPath) {
        this.annotationFilesPath = annotationFilesPath;
    }

    public String getSnpSamplesPath() {
        return snpSamplesPath;
    }

    public void setSnpSamplesPath(String snpSamplesPath) {
        this.snpSamplesPath = snpSamplesPath;
    }

    public String getScriptFilesPath() {
        return scriptFilesPath;
    }

    public void setScriptFilesPath(String scriptFilesPath) {
        this.scriptFilesPath = scriptFilesPath;
    }

    public Path getAbsoluteUploadFilesPath() {
        return Paths.get(getFlyvarRoot(), getUploadFilesPath()).toAbsolutePath();
    }

    public Path getAbsoluteAnnotationFilesPath() {
        return Paths.get(getFlyvarRoot(), getAnnotationFilesPath()).toAbsolutePath();
    }

    public Path getAbsoluteSnpSamplesPath() {
        return Paths.get(getFlyvarRoot(), getSnpSamplesPath()).toAbsolutePath();
    }

    public Path getAbsoluteScriptFilesPath() {
        return Paths.get(getFlyvarRoot(), getScriptFilesPath()).toAbsolutePath();
    }

}
