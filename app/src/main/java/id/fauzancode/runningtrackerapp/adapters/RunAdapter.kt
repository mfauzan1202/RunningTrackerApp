package id.fauzancode.runningtrackerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.ItemRunsBinding
import id.fauzancode.runningtrackerapp.db.Runs
import id.fauzancode.runningtrackerapp.utils.TrackingUtils
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(private val binding: ItemRunsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(run: Runs) {
            binding.apply {
                Glide.with(itemView).load(run.img).into(ivRunImage)
                val date = Calendar.getInstance().apply {
                    timeInMillis = run.timeStamp
                }
                val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                tvDate.text = dateFormat.format(date.time)

                tvAvgSpeed.text = itemView.resources.getString(
                    R.string.runs_avgSpeed,
                    run.avgSpeedInKMH.toString()
                )
                tvDistance.text = itemView.resources.getString(
                    R.string.runs_distance,
                    "${run.distanceInMeters / 1000f}"
                )
                tvTime.text = itemView.resources.getString(
                    R.string.runs_totalTime,
                    TrackingUtils.getFormattedStopWatchTime(run.timeInMillis)
                )
                tvCaloriesBurned.text = itemView.resources.getString(
                    R.string.runs_calsBurn,
                    run.caloriesBurned.toString()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunAdapter.RunViewHolder {
        val view = ItemRunsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunAdapter.RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.bind(run)
    }

    override fun getItemCount(): Int = differ.currentList.size

    val differCallback = object : DiffUtil.ItemCallback<Runs>() {
        override fun areItemsTheSame(oldItem: Runs, newItem: Runs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Runs, newItem: Runs): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Runs>) = differ.submitList(list)
}