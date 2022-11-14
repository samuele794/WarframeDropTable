package samuele794.it.warframe

import kotlinx.coroutines.test.runTest
import samuele794.it.feature.warframe.WarframeTableDownloader
import samuele794.it.feature.warframe.WarframeTableProcessor
import kotlin.test.Test
import kotlin.test.assertTrue

class WarframeTableDownloaderTest {

    val tableDownloader = WarframeTableDownloader("https://www.warframe.com/droptables")

    @Test
    fun testHtmlDownload() = runTest{
        val htmlPage = tableDownloader.downloadWebsite()
        assertTrue { htmlPage.isNotEmpty() }
    }

}