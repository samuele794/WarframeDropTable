package samuele794.it.feature.warframe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import samuele794.it.feature.warframe.model.*
import samuele794.it.feature.warframe.model.droptable.*

class WarframeTableProcessor(private val exceptionTable: WarframeTableException) {

    private val logger = LoggerFactory.getLogger(WarframeTableDownloader::class.java)

    suspend fun processMissionTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)

        val missionJob = async {
            val missionsTableListHtml = htmlPageParsed.select("h3#missionRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            return@async if (missionsTableListHtml != null) {
                processMissionTable(missionsTableListHtml)
            } else {
                null
            }
        }

        return@withContext missionJob.await()
    }

    private fun processMissionTable(missionsTableListHtml: List<List<Element>>): List<MissionDropTable> {
        val missionTableProcessed = missionsTableListHtml.let { missionsTableList ->
            val missionDropTableList = mutableListOf<MissionDropTable>()

            missionsTableList.forEach {
                var missionTable: MissionDropTable? = null
                var rotation: Rotation<Item>? = null

                logger.info("Evaluate new drop table")

                it.forEachIndexed { index, element ->
                    val item = element.firstElementChild()

                    if (item?.tag()?.normalName() == "th") {
                        val missionType = item.html().let {
                            MissionType.values().find { mission ->
                                it.contains("(${mission.missionName})")
                            }
                        }

                        if (missionType != null) {
                            //tag html is mission
                            missionTable = MissionDropTable(item.html(), missionType)
                            logger.info("Processing mission $missionTable")
                            if (!missionTable!!.hasRotationWithException(exceptionTable.missionRotationNoRotation)) {
                                rotation = Rotation("")
                            }
                        } else {
                            if (missionTable?.hasRotationWithException(exceptionTable.missionRotationNoRotation) == true) {
                                if (item.html().contains("Rotation")) {
                                    rotation?.let { mRotation -> missionTable = missionTable?.addRotation(mRotation) }
                                    rotation = Rotation(item.html())
                                    logger.info("Processing rotation $rotation")
                                }
                            } else {
                                //potentialy unused code
                                rotation = Rotation("")
                                logger.info("Processing rotation None")
                            }
                        }


                    } else {
                        val itemName = item?.html()
                        val percentage = item?.nextElementSibling()?.html()
                        rotation = rotation?.addItem(Item(itemName.orEmpty(), percentage.orEmpty()))
                        logger.info("Processing item $itemName")
                    }

                    if (index >= it.size - 1) {
                        missionTable = rotation?.let { mRotation -> missionTable?.addRotation(mRotation) }
                        logger.info("Save new mission table $missionTable")
                        missionTable?.let { it1 -> missionDropTableList.add(it1) }

                    }
                }
            }

            missionDropTableList.toList()
        }

//        Processor debug
//        val finalSet = mutableSetOf<Pair<String, String?>>()
//
//        val titles = missionsTableList.map {
//            it.first().children().first()!!.html()
//        }
//
//        val titles2 = missionTableProcessed.map {
//            it.missionName
//        }
//
//        titles.forEach { title ->
//            finalSet.add(
//                title to titles2.find { it.equals(title, ignoreCase = true) }
//            )
//        }

        return missionTableProcessed
    }

    suspend fun processRelicTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)


        val relicJob = async {
            val relicTableListHtml = htmlPageParsed.select("h3#relicRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            if (relicTableListHtml != null) {
                processRelicTable(relicTableListHtml)
            } else {
                null
            }
        }

        return@withContext relicJob.await()
    }


    private fun processRelicTable(relicTableListHtml: List<List<Element>>): List<RelicDropTable> {
        val relicTableProcessed = relicTableListHtml.let { relicTableList ->
            val relicRefinementList = mutableListOf<RelicRefinement>()

            relicTableList.forEach {
                var relicTable: RelicRefinement? = null

                logger.info("Evaluate new relic table")

                it.forEachIndexed { index, element ->
                    val item = element.firstElementChild()

                    if (item?.tag()?.normalName() == "th") {
                        relicTable = RelicRefinement(item.html())
                    } else {
                        val itemName = item?.html()
                        val percentage = item?.nextElementSibling()?.html()
                        relicTable = relicTable?.addItem(Item(itemName.orEmpty(), percentage.orEmpty()))
                        logger.info("Processing item $itemName")
                    }

                    if (index >= it.size - 1) {
                        logger.info("Save new relic table $relicTable")
                        relicTable?.let { it1 -> relicRefinementList.add(it1) }

                    }
                }
            }

            relicRefinementList
        }.windowed(4, 4).let { relicTableProcessed ->
            val regex = Regex(pattern = "\\(.*\\)", options = setOf(RegexOption.IGNORE_CASE))
            relicTableProcessed.map {
                val relicRefinementList = it.map { relicRefinementWithItem ->
                    val relicRefinementStringRange = regex.find(relicRefinementWithItem.relicRefinement)!!.range
                    val mRelicRefinement = relicRefinementWithItem.relicRefinement.substring(
                        relicRefinementStringRange.first + 1,
                        relicRefinementStringRange.last
                    )

                    relicRefinementWithItem.copy(
                        relicRefinement = mRelicRefinement
                    )
                }

                val relicName = it.first().relicRefinement
                RelicDropTable(
                    relicName = relicName.substring(0 until regex.find(relicName)!!.range.first)
                        .trim(),
                    relicRefinements = relicRefinementList
                )
            }
        }



        return relicTableProcessed
    }

    suspend fun processKeysTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)


        val keysJob = async {
            val keysTableListHtml = htmlPageParsed.select("h3#keyRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            if (keysTableListHtml != null) {
                processKeysTable(keysTableListHtml)
            } else {
                null
            }
        }

        return@withContext keysJob.await()
    }

    private fun processKeysTable(keysTableListHtml: List<List<Element>>): List<TableRotation<Item>> {
        val keysTableProcessed = keysTableListHtml.let { keysTableList ->
            val keysDropTableList = mutableListOf<TableRotation<Item>>()


            keysTableList.forEach {
                var keyTable: KeyDropTable? = null
                var rotation: Rotation<Item>? = null

                it.forEachIndexed { index, element ->
                    val item = element.firstElementChild()
                    if (item?.tag()?.normalName() == "th") {
                        if (item.html().startsWith("Rotation")) {
                            rotation?.let { mRotation ->
                                if (mRotation.name.isNotEmpty()) {
                                    keyTable = keyTable?.addRotation(mRotation) as KeyDropTable
                                }
                            }
                            rotation = Rotation(item.html())
                            logger.info("Processing rotation $rotation")
                        } else {
                            keyTable = KeyDropTable(item.html())
                            rotation = Rotation("")
                        }
                    } else {
                        val itemName = item?.html()
                        val percentage = item?.nextElementSibling()?.html()
                        rotation = rotation?.addItem(Item(itemName.orEmpty(), percentage.orEmpty()))
                        logger.info("Processing item $itemName")
                    }

                    if (index >= it.size - 1) {
                        keyTable = rotation?.let { mRotation -> keyTable?.addRotation(mRotation) as KeyDropTable }
                        logger.info("Save new mission table $keyTable")
                        keyTable?.let { it1 -> keysDropTableList.add(it1) }

                    }
                }
            }

            keysDropTableList
        }

        return keysTableProcessed
    }

    suspend fun processDynamicRewardTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)


        val dynamicJob = async {
            val keysTableListHtml = htmlPageParsed.select("h3#transientRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            if (keysTableListHtml != null) {
                processDynamicRewardTable(keysTableListHtml)
            } else {
                null
            }
        }

        return@withContext dynamicJob.await()
    }

    private fun processDynamicRewardTable(keysTableListHtml: List<List<Element>>): List<TableRotation<Item>> {
        val dynamicTableProcessed = keysTableListHtml.let { dynamicTableList ->
            val dynamicDropTableList = mutableListOf<TableRotation<Item>>()


            dynamicTableList.forEach {
                var keyTable: DynamicDropTable? = null
                var rotation: Rotation<Item>? = null

                it.forEachIndexed { index, element ->
                    val item = element.firstElementChild()
                    if (item?.tag()?.normalName() == "th") {
                        if (item.html().startsWith("Rotation")) {
                            rotation?.let { mRotation ->
                                if (mRotation.name.isNotEmpty()) {
                                    keyTable = keyTable?.addRotation(mRotation) as DynamicDropTable
                                }
                            }
                            rotation = Rotation(item.html())
                            logger.info("Processing rotation $rotation")
                        } else {
                            keyTable = DynamicDropTable(item.html())
                            rotation = Rotation("")
                        }
                    } else {
                        val itemName = item?.html()
                        val percentage = item?.nextElementSibling()?.html()
                        rotation = rotation?.addItem(Item(itemName.orEmpty(), percentage.orEmpty()))
                        logger.info("Processing item $itemName")
                    }

                    if (index >= it.size - 1) {
                        keyTable = rotation?.let { mRotation -> keyTable?.addRotation(mRotation) as DynamicDropTable }
                        logger.info("Save new mission table $keyTable")
                        keyTable?.let { it1 -> dynamicDropTableList.add(it1) }

                    }
                }


            }

            dynamicDropTableList
        }

        return dynamicTableProcessed
    }

    suspend fun processSortieRewardTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)


        val sortieJob = async {
            val sortieTableListHtml = htmlPageParsed.select("h3#sortieRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            if (sortieTableListHtml != null) {
                processSortieRewardTable(sortieTableListHtml)
            } else {
                null
            }
        }

        return@withContext sortieJob.await()
    }

    private fun processSortieRewardTable(keysTableListHtml: List<List<Element>>): List<TableRotation<Item>> {
        val sortieTableProcessed = keysTableListHtml.let { sortieTableList ->
            val sortieDropTableList = mutableListOf<TableRotation<Item>>()


            sortieTableList.forEach {
                var sortieTable: SortieDropTable? = null
                var rotation: Rotation<Item>? = null

                it.forEachIndexed { index, element ->
                    val item = element.firstElementChild()
                    if (item?.tag()?.normalName() == "th") {
                        sortieTable = SortieDropTable(item.html())
                        rotation = Rotation<Item>("")
                    } else {
                        val itemName = item?.html()
                        val percentage = item?.nextElementSibling()?.html()
                        rotation = rotation?.addItem(Item(itemName.orEmpty(), percentage.orEmpty()))
                        logger.info("Processing item $itemName")
                    }

                    if (index >= it.size - 1) {
                        sortieTable =
                            rotation?.let { mRotation -> sortieTable?.addRotation(mRotation) as SortieDropTable }
                        logger.info("Save new mission table $sortieTable")
                        sortieTable?.let { it1 -> sortieDropTableList.add(it1) }

                    }
                }
            }
            sortieDropTableList
        }

        return sortieTableProcessed
    }

    suspend fun processCetusBountyRewardTable(htmlPage: String) = withContext(Dispatchers.Default) {
        val htmlPageParsed = Jsoup.parse(htmlPage)


        val cetusJob = async {
            val cetusTableListHtml = htmlPageParsed.select("h3#cetusRewards").next().first()
                ?.children()
                ?.first()
                ?.children()
                ?.toList()
                ?.splitTables()

            if (cetusTableListHtml != null) {
                processCetusBountyRewardTable(cetusTableListHtml)
            } else {
                null
            }
        }

        return@withContext cetusJob.await()
    }

    private fun processCetusBountyRewardTable(keysTableListHtml: List<List<Element>>): List<TableRotation<Stage>> {
        val bontyTableProcessed = keysTableListHtml.let { bountyTableList ->
            val bountyDropTableList = mutableListOf<TableRotation<Stage>>()


            bountyTableList.forEach {
                var bountyTable: BountyDropTable? = null
                var rotation: Rotation<Stage>? = null
                var stage: Stage? = null

                it.map { element ->
                    Elements(element.children().toMutableList().filter { mElement -> mElement.html().isNotEmpty() })
                }.forEachIndexed { index, elements ->
                    if (elements.size > 1) {
                        stage = stage?.addItem(Item(elements[0].html(), elements[1].html()))
                    } else {
                        val item = elements.first()
                        if (item != null) {
                            if (item.tag().name == "th") {
                                when {
                                    item.html().startsWith("Rotation") -> {
                                        var mRotation = rotation
                                        if (mRotation != null) {
                                            val mStage = stage
                                            if (mStage != null) {
                                                mRotation = mRotation.addItem(mStage)
                                            }
                                            bountyTable = bountyTable?.addRotation(mRotation) as BountyDropTable
                                            rotation = null
                                            stage = null
                                        }
                                        rotation = Rotation(item.html())
                                    }

                                    item.html().startsWith("Stage") || item.html().endsWith("Stage") -> {
                                        val mStage = stage
                                        if (mStage != null) {
                                            rotation = rotation?.addItem(mStage)
                                            stage = null
                                        }
                                        stage = Stage(item.html())
                                    }

                                    else -> {
                                        var mBountyTable = bountyTable
                                        if (mBountyTable != null) {
                                            val mStage = stage
                                            var mRotation = rotation
                                            if (mStage != null && mRotation != null) {
                                                mRotation = mRotation.addItem(mStage)
                                                mBountyTable = mBountyTable.addRotation(mRotation) as BountyDropTable
                                            }
                                            bountyDropTableList.add(mBountyTable)

                                            stage = null
                                            rotation = null
                                        }
                                        bountyTable = BountyDropTable(item.html())
                                    }
                                }
                            }
                        }

                    }

                    if (index >= it.size - 1) {
                        var mBountyTable = bountyTable
                        if (mBountyTable != null) {
                            val mStage = stage
                            var mRotation = rotation
                            if (mStage != null && mRotation != null) {
                                mRotation = mRotation.addItem(mStage)
                                mBountyTable = mBountyTable.addRotation(mRotation) as BountyDropTable
                            }
                            bountyDropTableList.add(mBountyTable)

                            stage = null
                            rotation = null
                        }
                        mBountyTable =
                            rotation?.let { mRotation -> mBountyTable?.addRotation(mRotation) as BountyDropTable }
                        logger.info("Save new mission table $mBountyTable")
                        mBountyTable?.let { it1 -> bountyDropTableList.add(it1) }

                    }
                }
            }
            bountyDropTableList
        }

        return bontyTableProcessed
    }

    private fun List<Element>.splitTables(): List<List<Element>> {
        val targetTable = mutableListOf<List<Element>>()
        var sourceTable = mutableListOf<Element>()
        forEach {
            if (it.tag().normalName() == "tr" && it.attr("class") == "blank-row") {
                targetTable.add(sourceTable)
                sourceTable = mutableListOf()
            } else {
                sourceTable.add(it)
            }
        }
        return targetTable.toList()
    }
}