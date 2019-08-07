package varunbehl.algebra.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import varunbehl.algebra.R
import varunbehl.algebra.databinding.ButtonLayoutBinding


class NumbersAdapter(private val list: MutableList<Int>, val mListener: NumberListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LARGE_CARD_TYPE: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            LARGE_CARD_TYPE -> {
                val binding: ButtonLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.button_layout,
                    parent, false
                )
                return LargeCardViewHolder(binding)
            }
            else -> {
                val binding: ButtonLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.button_layout,
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
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return LARGE_CARD_TYPE
    }


    inner class LargeCardViewHolder(val binding: ButtonLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                list[adapterPosition].let { it1 -> mListener.onNumberClick(it1) }
            }
        }

        fun bind() {
            val item = list[adapterPosition]
            binding.button12.text=item.toString()
        }
    }


    interface NumberListener{
      fun  onNumberClick(number: Int)
    }

}