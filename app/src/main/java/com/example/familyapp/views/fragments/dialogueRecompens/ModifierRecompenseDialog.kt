package com.example.familyapp.views.fragments.dialogueRecompens

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.familyapp.R
import com.example.familyapp.data.model.recompense.Recompense

class ModifierRecompenseDialog : DialogFragment() {
    interface OnRecompenseModifieeListener {
        fun onRecompenseModifiee(id: Int, nom: String, description: String, cout: Int, stock: Int)
    }

    private var listener: OnRecompenseModifieeListener? = null

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_NOM = "nom"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_COUT = "cout"
        private const val ARG_STOCK = "stock"

        fun newInstance(recompense: Recompense) = ModifierRecompenseDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, recompense.idRecompense)
                putString(ARG_NOM, recompense.nom)
                putString(ARG_DESCRIPTION, recompense.description)
                putInt(ARG_COUT, recompense.cout)
                putInt(ARG_STOCK, recompense.stock)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnRecompenseModifieeListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments?.getInt(ARG_ID) ?: 0
        val nom = arguments?.getString(ARG_NOM) ?: ""
        val description = arguments?.getString(ARG_DESCRIPTION) ?: ""
        val cout = arguments?.getInt(ARG_COUT) ?: 0
        val stock = arguments?.getInt(ARG_STOCK) ?: 0

        val view = layoutInflater.inflate(R.layout.dialog_modifier_recompense, null)

        view.findViewById<EditText>(R.id.etNom).setText(nom)
        view.findViewById<EditText>(R.id.etDescription).setText(description)
        view.findViewById<EditText>(R.id.etCout).setText(cout.toString())
        view.findViewById<EditText>(R.id.etStock).setText(stock.toString())

        return AlertDialog.Builder(requireContext())
            .setTitle("Modifier la récompense")
            .setView(view)
            .setPositiveButton("Enregistrer") { _, _ ->
                val newNom = view.findViewById<EditText>(R.id.etNom).text.toString()
                val newDescription = view.findViewById<EditText>(R.id.etDescription).text.toString()
                val newCout = view.findViewById<EditText>(R.id.etCout).text.toString().toIntOrNull() ?: 0
                val newStock = view.findViewById<EditText>(R.id.etStock).text.toString().toIntOrNull() ?: 0

                listener?.onRecompenseModifiee(id, newNom, newDescription, newCout, newStock)
            }
            .setNegativeButton("Annuler", null)
            .create()
    }
}