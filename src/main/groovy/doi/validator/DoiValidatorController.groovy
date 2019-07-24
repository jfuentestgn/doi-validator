package doi.validator

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/validator/doi")
class DoiValidatorController {

    static final Logger LOG = LoggerFactory.getLogger(DoiValidatorController.class)

    DoiValidatorService  doiValidatorService

    DoiValidatorController(DoiValidatorService doiValidatorService) {
        this.doiValidatorService = doiValidatorService
    }

    /**
     *
     * @param doi Documento Oficial de Identidad. Value to be validated
     * @return Valitation result
     */
    @Get(value = "/{doi}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Validates a DOI", description = "Validates a DOI value and returns its type and whether is valid or not")
    public DoiValidInfo validate(String doi) {
        LOG.info("Before calling service")
        long t0 = System.currentTimeMillis()
        DoiValidInfo doiValidInfo = this.doiValidatorService.validateDoi(doi)
        long t1 = System.currentTimeMillis()
        LOG.info("After calling service. Time = " + (t1 - t0) + " msecs")
        return doiValidInfo;
    }
}
