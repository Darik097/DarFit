import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class CenterZoomLayoutManager(context: Context) : LinearLayoutManager(context, VERTICAL, false) {

    private val shrinkAmount = 0.15f
    private val shrinkDistance = 0.9f
    private val minAlpha = 0.7f // Минимальная прозрачность для невыбранных элементов

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val midpoint = height / 2f

        // Изменяем масштаб и прозрачность элементов в зависимости от их расстояния до центра
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val childMidpoint = (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f
            val distance = min(shrinkDistance, abs(midpoint - childMidpoint) / midpoint)
            val scale = 1f - shrinkAmount * distance

            // Устанавливаем масштаб
            child.scaleX = scale
            child.scaleY = scale

            // Устанавливаем прозрачность с минимальным значением minAlpha
            child.alpha = minAlpha + (1f - minAlpha) * (1f - distance)
        }

        return super.scrollVerticallyBy(dy, recycler, state)
    }
}

