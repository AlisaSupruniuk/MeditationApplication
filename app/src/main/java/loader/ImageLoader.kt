package loader

import android.widget.ImageView
import coil.load

class ImageLoader: ImageLoaderInterface {

    override fun load(url: String, view: ImageView) {
        view.load(url)
    }

    override fun load(url: String, view: ImageView, errorResId: Int) {
        view.load(url) { error(errorResId) }
    }
}