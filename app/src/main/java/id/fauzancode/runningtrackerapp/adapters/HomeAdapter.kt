package id.fauzancode.runningtrackerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.fauzancode.runningtrackerapp.databinding.ItemProgressBinding
import id.fauzancode.runningtrackerapp.db.Statistics

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(private val binding: ItemProgressBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Statistics) {
            binding.apply {
                tvTitle.text = data.Title
                tvPoints.text = data.Value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val statistics = differ.currentList[position]
        holder.bind(statistics)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Statistics>() {
        override fun areItemsTheSame(oldItem: Statistics, newItem: Statistics): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Statistics, newItem: Statistics): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Statistics>) = differ.submitList(list)
}