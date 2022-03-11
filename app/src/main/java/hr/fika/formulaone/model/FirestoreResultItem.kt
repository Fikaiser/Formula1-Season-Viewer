package hr.fika.formulaone.model

import FastestLap


data class FirestoreResultItem(
    val number: Int = 0,
    val position: Int = 0,
    val positionText: String = "",
    val points: Double = 0.0,
    val driverId: String = "",
    val constructorId: String = "",
    val grid: Int = 0,
    val laps: Int = 0,
    val status: String = "",
    val time: String = "",
    val fastestLap: FastestLap = FastestLap()
)