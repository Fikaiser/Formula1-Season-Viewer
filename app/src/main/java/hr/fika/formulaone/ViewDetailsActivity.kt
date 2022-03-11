package hr.fika.formulaone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fika.formulaone.databinding.ActivityViewDetailsBinding
import hr.fika.formulaone.model.Item
import hr.fika.formulaone.model.ItemHolderSingleton

const val ITEM_POSITION = "hr.fika.formulaone.item_position"

class ViewDetailsActivity : AppCompatActivity() {


    private lateinit var items: MutableList<Item>
    private var itemPosition = 0
    private lateinit var binding: ActivityViewDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    private fun initPager() {
        items = ItemHolderSingleton.items
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        binding.viewPager.adapter = ViewDetailsAdapter(this, items)
        binding.viewPager.currentItem = itemPosition
    }
}