package by.supruniuk.alisa.meditationapplication.ui.main.learn

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.supruniuk.alisa.meditationapplication.databinding.ItemVideoBinding
import by.supruniuk.alisa.meditationapplication.models.VideoModel

class LearnAdapter(private val onItemListener: (item: VideoModel) -> Unit) :
    ListAdapter<VideoModel, LearnAdapter.VideoViewHolder>(VideoDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.card.setOnClickListener { onItemListener(currentList[bindingAdapterPosition]) }
        }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: VideoModel) {
            binding.imageRecyclerView.setImageResource(item.image)
            binding.textRecyclerView1.text = item.title
            binding.textRecyclerView3.text = "Duration: ${item.duration}"
        }
    }
}

class VideoDiffCallBack : DiffUtil.ItemCallback<VideoModel>() {
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
        oldItem.url == newItem.url

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
        oldItem == newItem
}