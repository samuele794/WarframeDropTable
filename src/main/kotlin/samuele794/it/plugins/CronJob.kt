package samuele794.it.plugins

import io.ktor.server.application.*
import kjob.core.kjob
import kjob.inmem.InMem
import kjob.kron.KronModule

fun Application.configureCronJob() {
    val kJob = kjob(InMem){
        extension(KronModule)
    }
}