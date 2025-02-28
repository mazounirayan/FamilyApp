package com.example.familyapp.views.fragments.Conversation

import ConversationsViewModel
import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.views.Adapters.ConversationsAdapter

class SwipeToDeleteCallback(private val adapter: ConversationsAdapter, private val viewModel: ConversationsViewModel, private val userId: Int, private val lifecycleOwner: LifecycleOwner) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val conversation = adapter.getConversationAt(position)
        AlertDialog.Builder(adapter.context)
            .setTitle("Quitter le groupe")
            .setMessage("Êtes-vous sûr de vouloir quitter ce groupe ?")
            .setPositiveButton("Oui") { _, _ ->
                viewModel.quitChat(userId, conversation.id)
                viewModel.chatQuitStatus.observe(lifecycleOwner) { success ->
                    if (success) {
                        adapter.deleteItem(position)
                    } else {
                        Toast.makeText(adapter.context, "Erreur lors de la sortie du groupe", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemChanged(position)
                    }
                }

                adapter.deleteItem(position)
            }
            .setNegativeButton("Non") { _, _ ->
                adapter.notifyItemChanged(position)
            }
            .show()
    }

}
