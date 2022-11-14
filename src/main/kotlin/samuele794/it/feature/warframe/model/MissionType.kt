package samuele794.it.feature.warframe.model


enum class MissionType(val missionName: String, val hasRotation: Boolean) {
    Survival("Survival", true),
    Capture("Capture", false),
    Defense("Defense", true),
    Rescue("Rescue", true),
    Caches("Caches", true),
    Assassination("Assassination", false),
    Exterminate("Exterminate", false),
    Interception("Interception", true),
    Spy("Spy", true),
    Skirmish("Skirmish", false),
    Excavation("Excavation", true),
    MobileDefense("Mobile Defense", false),
    Disruption("Disruption", true),
    Defection("Defection", true),
    Arena("Arena", true),
    InfestedSalvage("Infested Salvage", true),
    Pursuit("Pursuit", false),
    Rush("Rush", true),
    Sabotage("Sabotage", false),
    VoidArmageddon("Void Armageddon", true),
    VoidCascade("Void Cascade", true),
    VoidFlood("Void Flood", true),
    SanctuaryOnslaught("Sanctuary Onslaught", true),

    //Verificare se tenerlo
    Conclave("Conclave", true);
}