package varunbehl.algebra.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import varunbehl.algebra.R
import varunbehl.algebra.databinding.ItemLayoutBinding
import varunbehl.algebra.ui.model.GameModel
import java.util.*


class HomeCardAdapter(private val list: ArrayList<GameModel>?,val  mListener: HomeCardListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LARGE_CARD_TYPE: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            LARGE_CARD_TYPE -> {
                val binding: ItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_layout,
                    parent, false
                )
                return LargeCardViewHolder(binding)
            }
            else -> {
                val binding: ItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_layout,
                    parent, false
                )
                return LargeCardViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as LargeCardViewHolder).bind()

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return LARGE_CARD_TYPE
    }


    inner class LargeCardViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                mListener.onCardClick(list?.get(adapterPosition)!!)
            }
        }

        fun bind() {
            val item = list?.get(adapterPosition)
            binding.txTittle.text=item?.name.toString()
            binding.ivRestaurantThumb.setImageDrawable(binding.root.context.resources.getDrawable(item?.drawableid!!))
        }
    }


    interface  HomeCardListener{
      fun  onCardClick(gameModel: GameModel)
    }

}