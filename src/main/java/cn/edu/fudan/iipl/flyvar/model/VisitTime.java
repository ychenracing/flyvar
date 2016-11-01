/**
 * ychen. Copyright (c) 2016年10月24日.
 */
package cn.edu.fudan.iipl.flyvar.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 总访问次数
 * 
 * @author racing
 * @version $Id: VisitTime.java, v 0.1 2016年10月24日 下午10:11:30 racing Exp $
 */
@Document(collection = "visittime")
public class VisitTime implements Serializable {

    private static final long serialVersionUID = 7973571957670100451L;

    @Id
    private String            id;

    private Long              time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
