package hr.fika.formulaone.model

import Circuit
import FastestLap
import Results


data class RaceItem(
    var _id: Int = 0,
    var season: Int = 0,
    var round: Int = 0,
    var url: String = "",
    var raceName: String = "",
    var circuit: Circuit = Circuit(),
    var date: String = "",
    var time: String = "",
    var results: List<Results> = listOf(),
    var watched: Boolean = false
) {
    fun serializeForFirestore(): FirestoreRaceItem {

        var fri = FirestoreRaceItem()
        fri._id = this._id
        fri.circuit = this.circuit
        fri.date = this.date
        fri.raceName = this.raceName
        fri.round = this.round
        fri.season = this.season
        fri.time = this.time
        fri.url = this.url
        fri.watched = this.watched
        val list: MutableList<FirestoreResultItem> = mutableListOf()
        for (it in this.results) {

            var time: String = if (it.time != null)
                it.time.time
            else
                ""
            var fastestLap: FastestLap = it.fastestLap ?: FastestLap()

            val test = FirestoreResultItem(
                it.number, it.position, it.positionText, it.points, it.driver.driverId,
                it.constructor.constructorId, it.grid, it.laps, it.status, time, fastestLap
            )
            list.add(test)
        }
        fri.results = list

        return fri
    }
}
