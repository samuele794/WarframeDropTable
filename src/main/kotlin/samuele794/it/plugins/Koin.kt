package samuele794.it.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
//        modules(helloAppModule)
    }

}
