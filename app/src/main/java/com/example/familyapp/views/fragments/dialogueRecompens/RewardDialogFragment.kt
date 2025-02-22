package com.example.familyapp.views.fragments.dialogueRecompens

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.familyapp.R

class AjoutRecompenseDialog : DialogFragment() {
    interface OnRecompenseAjouteeListener {
        fun onRecompenseAjoutee(nom: String, description: String, cout: Int, stock: Int,estDisponible:Boolean)
    }

    private var listener: OnRecompenseAjouteeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnRecompenseAjouteeListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_ajout_recompense, null)

        return AlertDialog.Builder(requireContext())
            .setTitle("Ajouter une récompense")
            .setView(view)
            .setPositiveButton("Ajouter") { _, _ ->
                val nom = view.findViewById<EditText>(R.id.etNom).text.toString()
                val description = view.findViewById<EditText>(R.id.etDescription).text.toString()
                val cout = view.findViewById<EditText>(R.id.etCout).text.toString().toIntOrNull() ?: 0
                val stock = view.findViewById<EditText>(R.id.etStock).text.toString().toIntOrNull() ?: 0
                val switchDisponible = view.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchDisponible)


                // Validation des champs
                if (nom.isBlank() || description.isBlank()) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                } else {
                    listener?.onRecompenseAjoutee(nom, description, cout, stock,      switchDisponible.isChecked)
                    dismiss()
                }

            }
            .setNegativeButton("Annuler", null)
            .create()
    }
}