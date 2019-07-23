package doi.validator

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum DoiType {

    DNI("DNI", "DNI", true, true),
    NIE("NIE", "NIE", true, false),
    NIFPJ("NIF", "NIF-PJ", false, true)


    String type
    String description
    boolean pf
    boolean national

    DoiType(String type, String description, boolean pf, boolean national) {
        this.type = type;
        this.description = description;
        this.pf = pf;
        this.national = national;
    }

}