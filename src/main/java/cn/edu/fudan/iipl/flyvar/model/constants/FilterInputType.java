package cn.edu.fudan.iipl.flyvar.model.constants;

public enum FilterInputType {
    
    FOUR_COLUMN_TAB_SEPARATED(1, "4 column tab separated"),
    VCF_FORMAT(2, "vcf format");
    
    private int code;
    private String message;
    
    FilterInputType(int code, String message) {
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
    
    public static FilterInputType of(int code) {
        for (FilterInputType item : values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }
    
    
}
