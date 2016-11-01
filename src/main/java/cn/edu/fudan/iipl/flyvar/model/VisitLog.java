/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 访问记录
 * 
 * @author racing
 * @version $Id: VisitLog.java, v 0.1 2016年10月23日 下午3:27:07 racing Exp $
 */
@Document(collection = "visitlog")
public class VisitLog implements Serializable {

    private static final long serialVersionUID = 5582703622393189425L;

    @Id
    private String            id;

    private String            ipAddress;

    private String            visitTime;

    public VisitLog() {

    }

    public VisitLog(String ipAddress, String visitTime) {
        this.ipAddress = ipAddress;
        this.visitTime = visitTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
