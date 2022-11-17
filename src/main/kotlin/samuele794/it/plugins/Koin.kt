package samuele794.it.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import samuele794.it.feature.warframe.WarframeExceptionLoader
import samuele794.it.feature.warframe.WarframeTableDownloader
import samuele794.it.feature.warframe.WarframeTableProcessor
import java.io.File

fun Application.configureKoin() {
    install(Koin) {
        environment
        slf4jLogger()
        modules(getModules())
//        modules(helloAppModule)
    }
}

private fun Application.getModules() = buildList {
    add(getWarframeModules(environment.config))
}

private fun getWarframeModules(config: ApplicationConfig) = module {
    single {
        val exceptionFile = File(config.property("ktor.application.warframe.exceptionTablePath").getString())
        WarframeExceptionLoader(DefaultJson, exceptionFile.toURI())
    }
    single { WarframeTableDownloader(config.property("ktor.application.warframe.tableEndpoint").getString()) }

    factory { WarframeTableProcessor(get()) }
}
