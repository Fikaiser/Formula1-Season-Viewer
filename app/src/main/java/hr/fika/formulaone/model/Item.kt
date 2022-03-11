package hr.fika.formulaone.model

import android.os.Parcel
import android.os.Parcelable


data class Item(
    val raceName: String,
    val round: Int,
    val time: String,
    val date: String,
    val url: String,
    var watched: Boolean,
    val picturePath: String,
    val circuitName: String,
    val country: String,
    val locality: String,
    val lat: String,
    val long: String,
    val winnerName: String,
    val winnerLastname: String,
    val winnerNumber: Int,
    val winnerConstructor: String,
    val winnerTime: String,
    val winnerPicturePath: String,
    val secondName: String,
    val secondLastname: String,
    val secondNumber: Int,
    val secondConstructor: String,
    val secondTime: String,
    val secondPicturePath: String,
    val thirdName: String,
    val thirdLastname: String,
    val thirdNumber: Int,
    val thirdConstructor: String,
    val thirdTime: String,
    val thirdPicturePath: String
)
