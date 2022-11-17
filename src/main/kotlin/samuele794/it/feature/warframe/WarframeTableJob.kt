package samuele794.it.feature.warframe

import kjob.core.KronJob
import kotlinx.coroutines.awaitAll
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object WarframeTableJob : KronJob("WarframeTableJob", "0 0 0 L * ? *"), KoinComponent {
    private val tableDownloader = get<WarframeTableDownloader>()
    private val tableProcessor = get<WarframeTableProcessor>()

    suspend fun downloadAndProcess() {
        val wfWebsite = tableDownloader.downloadWebsite()
        val asyncList = listOf(
            tableProcessor.processMissionTable(wfWebsite),
            tableProcessor.processRelicTable(wfWebsite),
            tableProcessor.processKeysTable(wfWebsite),
            tableProcessor.processDynamicRewardTable(wfWebsite),
            tableProcessor.processCetusBountyRewardTable(wfWebsite),
        ).awaitAll()
    }
}