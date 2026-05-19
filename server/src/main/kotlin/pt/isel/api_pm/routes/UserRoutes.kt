package pt.isel.api_pm.routes

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.dto.toDTO
import pt.isel.api_pm.exceptions.ForbiddenException
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.UserService
import pt.isel.api_pm.utils.requireUIntParameter
import pt.isel.api_pm.utils.requireUserId

fun Route.userRoutes(
    service: UserService
) {

    get(Routes.Users.BASE) {

        call.respond(
            service.getUsers().map { user ->
                user.toDTO()
            }
        )
    }

    authenticate(AuthConfig.JWT_NAME) {

        get(Routes.Users.BY_ID) {

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val paramId =
                call.parameters["id"]
                    .requireUIntParameter("user ID")

            if (userId != paramId) {
                throw ForbiddenException()
            }

            call.respondText(
                "Successfully accessed user with ID: $paramId"
            )
        }
    }
}