package validator.doi;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/validator/doi")
public class ValidatorDoiController {
    private static final Logger LOG = LoggerFactory.getLogger(ValidatorDoiController.class);

    private DoiValidator doiValidator;

    public ValidatorDoiController(DoiValidator doiValidator) {
        this.doiValidator = doiValidator;
    }

    @Get(value = "/{doi}", produces = MediaType.APPLICATION_JSON)
    public DoiInfo validate(String doi) {
        LOG.info("Before calling service");
        long t0 = System.currentTimeMillis();
        DoiInfo result = this.doiValidator.parseDoi(doi);
        long t1 = System.currentTimeMillis();
        LOG.info("After calling service. Time = " + (t1 - t0) + " msecs");
        return result;
    }

}
