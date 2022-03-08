package ba.unsa.etf.zavrsniRad.routes

import ba.unsa.etf.zavrsniRad.data.checkIfUserExists
import ba.unsa.etf.zavrsniRad.data.collections.User
import ba.unsa.etf.zavrsniRad.data.registerUser
import ba.unsa.etf.zavrsniRad.data.requests.AccountRequest
import ba.unsa.etf.zavrsniRad.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute (){
    route("/register"){
        post{
            val request = try {
                call.receive<AccountRequest>() // zaprima tijelo zahtjeva i ubacuje iz Json u AccountRequest
            }catch (e: ContentTransformationException){// ako je pogrešan format zahtjeva
                call.respond(BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if(!userExists){
                if(registerUser(User(request.email, request.password))){
                    call.respond(OK,SimpleResponse(true,"Successfuly created account!"))
                }else{
                    call.respond(OK,SimpleResponse(false,"Unknown error ocured!"))
                }
            }else{
                call.respond(OK,SimpleResponse(false,"A user with that emailalready exists!"))
            }
        }

    }
}