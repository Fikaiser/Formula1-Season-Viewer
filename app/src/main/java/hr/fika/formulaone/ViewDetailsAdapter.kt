package hr.fika.formulaone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import hr.fika.formulaone.api.RACES_COLLECTION
import hr.fika.formulaone.model.Item

class ViewDetailsAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ViewDetailsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val ivTrack = itemView.findViewById<ImageView>(R.id.ivTrack)
        val ivWatched = itemView.findViewById<ImageView>(R.id.ivWatched)
        private val tvRaceName = itemView.findViewById<TextView>(R.id.tvRaceNameDetails)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvSeason = itemView.findViewById<TextView>(R.id.tvSeason)
        private val tvRound = itemView.findViewById<TextView>(R.id.tvRoundDetails)
        private val tvCircuit = itemView.findViewById<TextView>(R.id.tvCircuit)
        private val tvLocality = itemView.findViewById<TextView>(R.id.tvLocality)
        private val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)

        private val tvWinnerName = itemView.findViewById<TextView>(R.id.tvWinnerName)
        private val tvWinnerTime = itemView.findViewById<TextView>(R.id.tvWinnerTime)
        private val ivWinnerImage = itemView.findViewById<ImageView>(R.id.ivWinnerImage)

        private val tvSecondName = itemView.findViewById<TextView>(R.id.tvSecondName)
        private val tvSecondTime = itemView.findViewById<TextView>(R.id.tvSecondTime)
        private val ivSecondImage = itemView.findViewById<ImageView>(R.id.ivSecondImage)

        private val tvThirdName = itemView.findViewById<TextView>(R.id.tvThirdName)
        private val tvThirdTime = itemView.findViewById<TextView>(R.id.tvThirdTime)
        private val ivThirdImage = itemView.findViewById<ImageView>(R.id.ivThirdImage)


        fun bind(item: Item) {


            loadImageIntoView(item.picturePath, R.drawable.track, ivTrack)

            if (item.watched) {
                Picasso.get().load(R.drawable.view).into(ivWatched)
            } else {
                Picasso.get().load(R.drawable.hidden).into(ivWatched)
            }

            tvDate.text = item.date
            tvSeason.text = "2021"
            tvCircuit.text = item.circuitName
            tvLocality.text = item.locality
            tvCountry.text = ", ${item.country}"
            tvRound.text = item.round.toString()
            tvRaceName.text = item.raceName

            tvWinnerName.text = "${item.winnerName} ${item.winnerLastname}"
            tvWinnerTime.text = item.winnerTime

            loadImageIntoView(item.winnerPicturePath, R.drawable.helmet, ivWinnerImage)

            tvSecondName.text = "${item.secondName} ${item.secondLastname}"
            tvSecondTime.text = item.secondTime

            loadImageIntoView(item.secondPicturePath, R.drawable.helmet, ivSecondImage)

            tvThirdName.text = "${item.thirdName} ${item.thirdLastname}"
            tvThirdTime.text = item.thirdTime

            loadImageIntoView(item.thirdPicturePath, R.drawable.helmet, ivThirdImage)

        }

        private fun loadImageIntoView(
            picturePath: String,
            replacementImage: Int,
            imageView: ImageView?
        ) {

            try {
                Picasso.get().load(picturePath).error(replacementImage).into(imageView)

            } catch (ex: Exception) {
                Picasso.get().load(replacementImage).into(imageView)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_details, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.ivWatched.setOnClickListener {
            val item = items[position]
            item.watched = !item.watched
            Firebase.firestore.collection(RACES_COLLECTION).document(item.round.toString())
                .update(Item::watched.name, item.watched)
            notifyItemChanged(position)
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


}