package loader

import android.widget.ImageView

interface ImageLoaderInterface {
    fun load(url: String, view: ImageView)
    fun load(url: String, view: ImageView, errorResId: Int)
}