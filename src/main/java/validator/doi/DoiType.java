package validator.doi;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DoiType {
    DNI("DNI", "DNI", true, true),
    NIE("NIE", "NIE", true, false),
    NIFPJ("NIF", "NIF-PJ", false, true),
    ;

    private String type;
    private String description;
    private boolean pf;
    private boolean national;

    DoiType(String type, String description, boolean pf, boolean national) {
        this.type = type;
        this.description = description;
        this.pf = pf;
        this.national = national;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPf() {
        return pf;
    }

    public boolean isNational() {
        return national;
    }

}
