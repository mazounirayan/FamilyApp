import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.familyapp.R


import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.familyapp.data.model.recompense.Recompense

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class RecompenseAdapter(
    private val isParent: Boolean,
    private val onModifierClick: (Recompense) -> Unit,
    private val onSupprimerClick: (Recompense) -> Unit,
    private val onEchangerClick: (Recompense) -> Unit
) : ListAdapter<Recompense, RecompenseAdapter.RecompenseViewHolder>(
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

    inner class RecompenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnMenu: ImageButton = itemView.findViewById(R.id.btnMenu)
        private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvCout: TextView = itemView.findViewById(R.id.tvCout)
        private val chipStock: Chip = itemView.findViewById(R.id.chipStock)
        private val btnEchanger: MaterialButton = itemView.findViewById(R.id.btnEchanger)

        fun bind(recompense: Recompense) {
            tvNom.text = recompense.nom
            tvDescription.text = recompense.description
            tvCout.text = "${recompense.cout} Points"

            configureStockChip(recompense)
            configureEchangerButton(recompense)
            configureMenuButton(recompense)
        }

        private fun configureStockChip(recompense: Recompense) {
            chipStock.text = "Stock: ${recompense.stock}"
            chipStock.chipBackgroundColor = ColorStateList.valueOf(
                when {
                    recompense.stock > 5 -> ContextCompat.getColor(itemView.context, R.color.stock_color_available)
                    recompense.stock > 0 -> ContextCompat.getColor(itemView.context, R.color.stock_color_low)
                    else -> ContextCompat.getColor(itemView.context, R.color.stock_color_out)
                }
            )
        }
        private fun configureEchangerButton(recompense: Recompense) {
            btnEchanger.isEnabled = recompense.estDisponible && recompense.stock > 0
            btnEchanger.setOnClickListener {
                onEchangerClick(recompense)
            }
        }

        private fun configureMenuButton(recompense: Recompense) {
            btnMenu.visibility = if (isParent) View.VISIBLE else View.GONE
            btnMenu.setOnClickListener { view ->
                PopupMenu(view.context, view).apply {
                    inflate(R.menu.menu_recompense)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_modifier -> {
                                onModifierClick(recompense)
                                true
                            }
                            R.id.action_supprimer -> {
                                onSupprimerClick(recompense)
                                true
                            }
                            else -> false
                        }
                    }
                    show()
                }
            }
        }
    }
}

