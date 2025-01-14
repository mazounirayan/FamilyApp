import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.familyapp.R


import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.familyapp.data.model.recompense.Recompense

import androidx.recyclerview.widget.RecyclerView

class RecompenseAdapter : ListAdapter<Recompense, RecompenseAdapter.RecompenseViewHolder>(
    object : DiffUtil.ItemCallback<Recompense>() {
        override fun areItemsTheSame(oldItem: Recompense, newItem: Recompense): Boolean {
            return oldItem.idRecompense == newItem.idRecompense
        }

        override fun areContentsTheSame(oldItem: Recompense, newItem: Recompense): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecompenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recompense, parent, false)
        return RecompenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecompenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecompenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvCout: TextView = itemView.findViewById(R.id.tvCout)
        private val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        private val btnEchanger: Button = itemView.findViewById(R.id.btnEchanger)

        fun bind(recompense: Recompense) {
            tvNom.text = recompense.nom
            tvDescription.text = recompense.description
            tvCout.text = "${recompense.cout} points"
            tvStock.text = "Stock: ${recompense.stock}"

            btnEchanger.isEnabled = recompense.estDisponible && recompense.stock > 0
            btnEchanger.setOnClickListener {
                // Implémenter la logique d'échange ici
            }
        }
    }
}

