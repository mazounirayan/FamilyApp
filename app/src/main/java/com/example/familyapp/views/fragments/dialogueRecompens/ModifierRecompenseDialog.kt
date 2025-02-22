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
        fun onRecompenseModifiee(
            id: Int,
            nom: String,
            description: String,
            cout: Int,
            stock: Int,
            estDisponible: Boolean
        )
    }

    private var listener: OnRecompenseModifieeListener? = null
    private lateinit var recompense: Recompense

    companion object {
        fun newInstance(recompense: Recompense): ModifierRecompenseDialog {
            val fragment = ModifierRecompenseDialog()
            fragment.recompense = recompense
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnRecompenseModifieeListener
            ?: throw RuntimeException("$context must implement OnRecompenseModifieeListener")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_modifier_recompense, null)

        // Initialisation des champs avec les données existantes
        view.findViewById<EditText>(R.id.etNom).setText(recompense.nom)
        view.findViewById<EditText>(R.id.etDescription).setText(recompense.description)
        view.findViewById<EditText>(R.id.etCout).setText(recompense.cout.toString())
        view.findViewById<EditText>(R.id.etStock).setText(recompense.stock.toString())
        val switchDisponible = view.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchDisponible)

        return AlertDialog.Builder(requireContext())
            .setTitle("Modifier la récompense")
            .setView(view)
            .setPositiveButton("Enregistrer") { _, _ ->
                val newNom = view.findViewById<EditText>(R.id.etNom).text.toString()
                val newDescription = view.findViewById<EditText>(R.id.etDescription).text.toString()
                val newCout = view.findViewById<EditText>(R.id.etCout).text.toString().toIntOrNull() ?: 0
                val newStock = view.findViewById<EditText>(R.id.etStock).text.toString().toIntOrNull() ?: 0

                listener?.onRecompenseModifiee(
                    recompense.idRecompense,
                    newNom,
                    newDescription,
                    newCout,
                    newStock,
                    switchDisponible.isChecked
                )
                dismiss()
            }
            .setNegativeButton("Annuler", null)
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}