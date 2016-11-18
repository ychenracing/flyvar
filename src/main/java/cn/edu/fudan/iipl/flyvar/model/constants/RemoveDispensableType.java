package cn.edu.fudan.iipl.flyvar.model.constants;

public enum RemoveDispensableType {
    
    YES(1, "yes"),
    NO(2, "no");
    
    private int code;
    private String message;
    
    RemoveDispensableType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static RemoveDispensableType of(int code) {
        for (RemoveDispensableType item : values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }
}
