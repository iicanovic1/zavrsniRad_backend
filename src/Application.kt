package ba.unsa.etf.zavrsniRad

import ba.unsa.etf.zavrsniRad.data.checkPasswordForEmail
import ba.unsa.etf.zavrsniRad.data.collections.User
import ba.unsa.etf.zavrsniRad.data.registerUser
import ba.unsa.etf.zavrsniRad.routes.loginRoute
import ba.unsa.etf.zavrsniRad.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing){
        registerRoute()
        loginRoute()
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication){
        configureAuth()
    }

    // Registracija korinsika prilikom pokretanja servera u svrhe testiranja
    /*
    CoroutineScope(Dispatchers.IO).launch{
        registerUser(
            User(
                "abd@abc.com",
                "123456"
            )
        )
    }
    */

}

private fun Authentication.Configuration.configureAuth(){
    basic {
        realm = "Note Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if(checkPasswordForEmail(email,password)){
                UserIdPrincipal(email)
            }else null
        }
    }
}
