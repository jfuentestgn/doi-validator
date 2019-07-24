package doi.validator

import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*


@OpenAPIDefinition(
        info = @Info(
                title = "DOI Validator",
                version = "1.0",
                description = "DOI (Documento Oficial de Identidad) Validator",
                license = @License(name = "MIT", url = "http://foo.bar"),
                contact = @Contact(url = "http://github.com/jfuentestgn", name = "Juan Fuentes", email = "jfuentestgna@gmail.com")
        )
)
@CompileStatic
class Application {
    static void main(String[] args) {
        Micronaut.run(Application)
    }
}