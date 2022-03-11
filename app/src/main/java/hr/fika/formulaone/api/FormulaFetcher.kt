package hr.fika.formulaone.api


import Circuit
import Constructor
import Driver
import Results
import Season
import Time
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.fika.formulaone.DATA_LOADED
import hr.fika.formulaone.FormulaReceiver
import hr.fika.formulaone.framework.sendBroadcast
import hr.fika.formulaone.framework.setBooleanPreference
import hr.fika.formulaone.model.FirestoreRaceItem
import hr.fika.formulaone.model.Item
import hr.fika.formulaone.model.ItemHolderSingleton
import hr.fika.formulaone.model.RaceItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val RACES_COLLECTION = "races"
const val DRIVERS_COLLECTION = "drivers"
const val CONSTRUCTORS_COLLECTION = "constructors"

class FormulaFetcher(private val context: Context) {

    private var formulaApi: FormulaApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        formulaApi = retrofit.create(FormulaApi::class.java)
    }

    fun fetchItems() {
        val request = formulaApi.fetchItems()
        request.enqueue(object : Callback<Season> {
            override fun onResponse(call: Call<Season>, response: Response<Season>) {
                response.body()?.let { uploadItems(it) }
            }

            override fun onFailure(call: Call<Season>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(raceItems: List<RaceItem>) {


        GlobalScope.launch {

            val displayItems = mutableListOf<Item>()
            raceItems.forEach {

                displayItems.add(
                    Item(
                        it.raceName,
                        it.round,
                        it.time,
                        it.date,
                        it.url,
                        it.watched,
                        ImageFetcher.getImageUrl(context, it.url),
                        it.circuit.circuitName,
                        it.circuit.location.country,
                        it.circuit.location.locality,
                        it.circuit.location.lat,
                        it.circuit.location.long,
                        it.results[0].driver.givenName,
                        it.results[0].driver.familyName,
                        it.results[0].driver.permanentNumber,
                        it.results[0].constructor.name,
                        it.results[0].time.time,
                        ImageFetcher.getImageUrl(context, it.results[0].driver.url),
                        it.results[1].driver.givenName,
                        it.results[1].driver.familyName,
                        it.results[1].driver.permanentNumber,
                        it.results[1].constructor.name,
                        it.results[1].time.time,
                        ImageFetcher.getImageUrl(context, it.results[1].driver.url),
                        it.results[2].driver.givenName,
                        it.results[2].driver.familyName,
                        it.results[2].driver.permanentNumber,
                        it.results[2].constructor.name,
                        it.results[2].time.time,
                        ImageFetcher.getImageUrl(context, it.results[2].driver.url)
                    )
                )
            }
            displayItems.sortBy { it.round }
            ItemHolderSingleton.items = displayItems


            context.sendBroadcast<FormulaReceiver>()

        }

    }


    private fun uploadItems(it: Season) {
        val db = Firebase.firestore

        for (race in it.seasonData.raceTable.races) {

            val raceItem = RaceItem(
                race.round,
                race.season,
                race.round,
                race.url,
                race.raceName,
                race.circuit,
                race.date,
                race.time,
                race.results,
                false
            )
            raceItem.results.forEach() {
                db.collection(DRIVERS_COLLECTION).document(it.driver.driverId).set(it.driver)
                db.collection(CONSTRUCTORS_COLLECTION).document(it.constructor.constructorId)
                    .set(it.constructor)
            }


            db.collection(RACES_COLLECTION).document(raceItem._id.toString())
                .set(raceItem.serializeForFirestore())

        }
        context.setBooleanPreference(DATA_LOADED, true)
    }

    fun getDrivers() {
        Firebase.firestore.collection(DRIVERS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->

                getConstructors(result.toObjects(Driver::class.java))
            }
            .addOnFailureListener { exception ->
                Log.w("Error getting documents.", exception)
            }
    }

    private fun getConstructors(drivers: List<Driver>) {

        Firebase.firestore.collection(CONSTRUCTORS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->

                getRaces(drivers, result.toObjects(Constructor::class.java))
            }
            .addOnFailureListener { exception ->
                Log.w("Error getting documents.", exception)
            }
    }

    private fun getRaces(drivers: List<Driver>, constructors: List<Constructor>) {

        Firebase.firestore.collection(RACES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val races: List<RaceItem> = convertToRaceItems(
                    result.toObjects(FirestoreRaceItem::class.java),
                    drivers,
                    constructors
                )
                populateItems(races)


            }
            .addOnFailureListener { exception ->
                Log.w("Error getting documents.", exception)
            }
    }


    private fun convertToRaceItems(
        raceItems: List<FirestoreRaceItem>,
        drivers: List<Driver>,
        constructors: List<Constructor>
    ): List<RaceItem> {

        val list = mutableListOf<RaceItem>()

        for (raceItem in raceItems) {

            var race = RaceItem()
            race._id = raceItem._id
            race.round = raceItem.round
            race.season = raceItem.season
            race.date = raceItem.date
            race.time = raceItem.time
            race.url = raceItem.url
            race.raceName = raceItem.raceName
            race.watched = raceItem.watched
            race.circuit = raceItem.circuit ?: Circuit()


            val listOfRaces = mutableListOf<Results>()
            raceItem.results.forEach {
                listOfRaces.add(
                    Results(
                        it.number, it.position, it.positionText, it.points,
                        drivers.firstOrNull { driver -> driver.driverId == it.driverId }!!,
                        constructors.firstOrNull { constructor -> constructor.constructorId == it.constructorId }!!,
                        it.grid, it.laps, it.status, Time(it.time), it.fastestLap
                    )
                )
            }
            race.results = listOfRaces
            list.add(race)
        }

        return list
    }

}