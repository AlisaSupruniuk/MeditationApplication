package by.supruniuk.alisa.meditationapplication.ui.main.meditation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.supruniuk.alisa.meditationapplication.R
import by.supruniuk.alisa.meditationapplication.databinding.ItemMeditationCardBinding
import by.supruniuk.alisa.meditationapplication.models.MeditationModel
import loader.ImageLoader

class MeditationAdapter(private val onItemListener: (item: MeditationModel) -> Unit) :
    ListAdapter<MeditationModel, MeditationAdapter.MeditationViewHolder>(MeditationDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationViewHolder =
        MeditationViewHolder(
            ItemMeditationCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.card.setOnClickListener { onItemListener(currentList[bindingAdapterPosition]) }
        }

    override fun onBindViewHolder(holder: MeditationViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class MeditationViewHolder(val binding: ItemMeditationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MeditationModel) {
            if (item.name.contentEquals("Breath")) {
                binding.card.setBackgroundResource(R.drawable.breath_background)
            } else {
                binding.card.setBackgroundResource(R.drawable.meditation_background)
            }
            binding.card.name = item.name
            binding.card.secondName = item.last_name
        }
    }
}

class MeditationDiffCallBack : DiffUtil.ItemCallback<MeditationModel>() {
    override fun areItemsTheSame(oldItem: MeditationModel, newItem: MeditationModel): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: MeditationModel, newItem: MeditationModel): Boolean =
        (oldItem) == newItem
}
