package samuele794.it

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import org.slf4j.LoggerFactory
import samuele794.it.plugins.*

fun main() {
    embeddedServer(
        Tomcat,
        environment = applicationEngineEnvironment {
            log = LoggerFactory.getLogger("ktor.application")
            config = HoconApplicationConfig(ConfigFactory.load())
            module(Application::module)
        }
    ).start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureCronJob()
    configureKoin()
}
