package by.supruniuk.alisa.meditationapplication.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import by.supruniuk.alisa.meditationapplication.databinding.AudioCardViewBinding

class AudioCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding: AudioCardViewBinding =
        AudioCardViewBinding.inflate(LayoutInflater.from(context), this)

    var name: String = ""
        set(value) {
            binding.name.text = value
            field = value
        }

    var secondName: String = ""
        set(value) {
            binding.secondName.text = value
            field = value
        }

    var image: ImageView = binding.image
}