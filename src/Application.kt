package ba.unsa.etf.zavrsniRad

import ba.unsa.etf.zavrsniRad.data.collections.User
import ba.unsa.etf.zavrsniRad.data.registerUser
import ba.unsa.etf.zavrsniRad.routes.registerRoute
import io.ktor.application.*
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
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }



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

