package samuele794.it.warframe

import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import samuele794.it.feature.warframe.WarframeExceptionLoader
import samuele794.it.feature.warframe.WarframeTableProcessor
import samuele794.it.testResourcePath
import kotlin.test.BeforeTest
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


class WarframeTableProcessorTest {

    lateinit var tableProcessor: WarframeTableProcessor

    private val testFileList = testResourcePath.toFile().listFiles()!!

    @BeforeTest
    fun initTest() {
        val exceptionTable = testFileList.find { it.name == "warframeExceptionTable.json" }!!
        val wfExceptionLoader = WarframeExceptionLoader(DefaultJson, exceptionTable.toURI())
        tableProcessor = WarframeTableProcessor(wfExceptionLoader.loadExceptionTable())
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessMissionRewards() = runTest {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processMissionTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessRelicRewards() = runTest {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processRelicTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessKeyRewards() = runTest {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processKeysTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessDynamicRewards() = runTest {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processDynamicRewardTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessSortieRewards() = runTest {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processSortieRewardTable(htmlPage.reader().readText())
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessCetusBountyRewards() = runTest(dispatchTimeoutMs = 5000000_00L) {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processCetusBountyRewardTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessSolarisBountyRewards() = runTest(dispatchTimeoutMs = 5000000_00L) {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processSolarisBountyRewardTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testProcessProfitTakerBountyRewards() = runTest(dispatchTimeoutMs = 5000000_00L) {
        val htmlPage = testFileList.find { it.name == "warframeDrops.html" }!!

        val executionTime = measureTime {
            tableProcessor.processProfitTakerBountyRewardTable(htmlPage.reader().readText()).await()
        }

        println(executionTime.toString())

    }


}