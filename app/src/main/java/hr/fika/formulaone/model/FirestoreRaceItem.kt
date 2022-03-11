package hr.fika.formulaone.model

import Circuit

data class FirestoreRaceItem(
    var _id: Int = 0,
    var season: Int = 0,
    var round: Int = 0,
    var url: String = "",
    var raceName: String = "",
    var circuit: Circuit? = null,
    var date: String = "",
    var time: String = "",
    var results: List<FirestoreResultItem> = listOf(),
    var watched: Boolean = false
)
