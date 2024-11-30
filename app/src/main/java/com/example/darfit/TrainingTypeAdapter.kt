import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.R
import com.example.darfit.Workout
import com.bumptech.glide.Glide

class TrainingTypeAdapter(
    private val data: List<Workout>,
    private val onItemClick: (Workout) -> Unit
) : RecyclerView.Adapter<TrainingTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = data[position]

        holder.nameTextView.text = workout.name

        Glide.with(holder.itemView.context)
            .load(workout.imageUrl)
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_1)
            .into(holder.backgroundImageView)

        holder.itemView.setOnClickListener {
            onItemClick(workout)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.overlayText)
        val backgroundImageView: ImageView = itemView.findViewById(R.id.backgroundImage)
    }
}




