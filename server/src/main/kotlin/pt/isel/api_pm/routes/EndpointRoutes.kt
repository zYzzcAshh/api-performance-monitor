package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.endpoint.toDTO
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.utils.requireUIntParameter
import pt.isel.api_pm.utils.requireUserId

fun Route.endpointRoutes(
    service: EndpointService
) {

    authenticate(AuthConfig.JWT_NAME) {

        post(Routes.Endpoints.BASE) {

            val request =
                call.receive<CreateEndpointRequest>()

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val url =
                EndpointUrl(request.url)

            val interval =
                IntervalSeconds(request.intervalSeconds)

            service.add(
                userId,
                url,
                request.name,
                request.method,
                interval,
                request.notification,
                request.alertRule
            )

            call.respond(
                HttpStatusCode.Created
            )
        }

        get(Routes.Endpoints.BASE) {

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val endpoints =
                service.getByUser(userId)

            call.respond(
                endpoints.toDTO()
            )
        }

        delete(Routes.Endpoints.DELETE) {

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val endpointId =
                call.parameters["id"]
                    .requireUIntParameter("endpoint ID")

            service.delete(
                userId,
                endpointId
            )

            call.respond(
                HttpStatusCode.OK
            )
        }
    }
}