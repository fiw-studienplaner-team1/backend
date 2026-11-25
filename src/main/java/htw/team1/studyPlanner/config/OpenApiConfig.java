package htw.team1.studyPlanner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zentrale Konfiguration fuer die API-Dokumentation (Swagger UI / springdoc).
 * <p>
 * Endpunkte, Parameter und DTO-Schemas liest springdoc automatisch aus den
 * Controllern aus. Diese Klasse ist nur fuer Angaben noetig, die NICHT im Code
 * stehen: Titel, Beschreibung und Version der API (oben in Swagger UI) sowie
 * spaeter das Login-Verfahren.
 * <p>
 * Swagger UI: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig
{

    @Bean
    public OpenAPI studyPlannerOpenAPI()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Studienplaner API")
                        .description("REST-API des Studienplaners. Jeder Endpunkt laesst sich hier ueber \"Try it out\" direkt ausprobieren.")
                        .version("1.0"));

        // TODO: Sobald Spring Security / Login eingebaut ist:
        //  1. Sicherheitsschema hier ergaenzen (z. B. bei JWT: .addSecurityItem(...)
        //     und .components(...addSecuritySchemes("bearerAuth", ...))).
        //     Dann erscheint in Swagger UI der Button "Authorize", ueber den man
        //     den Token eingibt und geschuetzte Endpunkte testen kann.
        //  2. In der SecurityConfig die Swagger-Pfade freigeben:
        //     "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**" -> permitAll()
        //     sonst ist Swagger UI selbst hinter dem Login gesperrt.
    }
}