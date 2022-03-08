package ba.unsa.etf.zavrsniRad.routes

import ba.unsa.etf.zavrsniRad.data.checkPasswordForEmail
import ba.unsa.etf.zavrsniRad.data.requests.AccountRequest
import ba.unsa.etf.zavrsniRad.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute(){
    route("/login"){
        post {
            val request = try {
                call.receive<AccountRequest>() // zaprima tijelo zahtjeva i ubacuje iz Json u AccountRequest
            }catch (e: ContentTransformationException){// ako je pogrešan format zahtjeva
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
            if(isPasswordCorrect){
                call.respond(OK, SimpleResponse(true,"You are now logged in!"))
            }else{
                call.respond(OK, SimpleResponse(false,"The email or password is incorrect!"))
            }
        }
    }
}