package samuele794.it.plugins

import io.ktor.server.application.*
import kjob.core.job.JobExecutionType
import kjob.core.kjob
import kjob.inmem.InMem
import kjob.kron.Kron
import kjob.kron.KronModule
import samuele794.it.feature.warframe.WarframeTableJob

fun Application.configureCronJob() {
    val kJob = kjob(InMem) {
        extension(KronModule)
    }.start()

    kJob(Kron).kron(WarframeTableJob) {
        executionType = JobExecutionType.NON_BLOCKING

        execute {
            it.downloadAndProcess()
        }
    }

}