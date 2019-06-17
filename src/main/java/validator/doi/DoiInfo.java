package validator.doi;


public class DoiInfo {


    private String value;
    private DoiType type;
    private String pfx;
    private String sfx;
    private String num;
    private int modulus;
    private String validLetter;
    private boolean valid;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DoiType getType() {
        return type;
    }

    public void setType(DoiType type) {
        this.type = type;
    }

    public String getPfx() {
        return pfx;
    }

    public void setPfx(String pfx) {
        this.pfx = pfx;
    }

    public String getSfx() {
        return sfx;
    }

    public void setSfx(String sfx) {
        this.sfx = sfx;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getModulus() {
        return modulus;
    }

    public void setModulus(int modulus) {
        this.modulus = modulus;
    }

    public String getValidLetter() {
        return validLetter;
    }

    public void setValidLetter(String validLetter) {
        this.validLetter = validLetter;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
