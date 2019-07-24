package doi.validator

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "DoiValidInfo", description = "Result of a DOI Validation")
class DoiValidInfo {

    String value
    DoiType type
    boolean valid

    String pfx
    String sfx
    String num
    int modulus
    String validLetter


}
