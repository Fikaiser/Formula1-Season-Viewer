package hr.fika.formulaone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.fika.formulaone.framework.startActivity
import hr.fika.formulaone.model.Item

class ItemAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvRound = itemView.findViewById<TextView>(R.id.tvRound)
        private val tvRaceName = itemView.findViewById<TextView>(R.id.tvRaceName)
        fun bind(item: Item) {

            try {
                Picasso.get().load(item.picturePath).error(R.drawable.track).into(ivItem)
            } catch (ex: Exception) {
                Picasso.get().load(R.drawable.track).into(ivItem)
            }
            tvRound.text = item.round.toString()
            tvRaceName.text = item.raceName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            context.startActivity<ViewDetailsActivity>(ITEM_POSITION, position)
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


}