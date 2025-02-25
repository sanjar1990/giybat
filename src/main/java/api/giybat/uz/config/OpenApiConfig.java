package api.giybat.uz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "G'iybat",
                        email = "something@gmal.com",
                        url = "https://kitabu.uz/"
                ),
                description = "This API exposes endpoints to manage tutorials.",
                title = "Kitabu uz Management API",
                version = "1.0",
                license = @License(
                        name = "Videohub",
                        url = "https://kitabu.uz/"

                ),
                termsOfService = "Savol javob guruhi: https://t.me/code_uz_group"
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server (
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @io.swagger.v3.oas.annotations.servers.Server(
                        description = "PROD ENV",
                        url = "https://kitabu.uz/"
                )
        }
//  this security is for every API
//        security = {
//                @SecurityRequirement(
//                        name = "bearerAuth"
//                )
//        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {
}
