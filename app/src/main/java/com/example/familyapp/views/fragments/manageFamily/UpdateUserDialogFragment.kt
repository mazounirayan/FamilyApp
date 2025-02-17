package com.example.familyapp.views.fragments.manageFamily

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.familyapp.R
import com.example.familyapp.data.model.user.UpdateUserRequest
import com.example.familyapp.data.model.user.User

class UpdateUserDialogFragment(private val user: User, private val onUserUpdated: (UpdateUserRequest) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.update_user_dialog, null)

        val etNom = view.findViewById<EditText>(R.id.et_nom)
        val etPrenom = view.findViewById<EditText>(R.id.et_prenom)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val spinnerRole = view.findViewById<Spinner>(R.id.spinner_role)

         etNom.setText(user.nom)
        etPrenom.setText(user.prenom)
        etEmail.setText(user.email)

         val roles = resources.getStringArray(R.array.role_options)
        spinnerRole.setSelection(roles.indexOf(user.role))

        view.findViewById<Button>(R.id.btn_update_user).setOnClickListener {
            val updateUserRequest = UpdateUserRequest(

                nom = etNom.text.toString(),
                prenom = etPrenom.text.toString(),
                email = etEmail.text.toString(),
                role = spinnerRole.selectedItem.toString()
            )
            onUserUpdated(updateUserRequest)
            dismiss()
        }


        builder.setView(view)
            .setTitle("Update User")
            .setNegativeButton("Cancel", { dialog, id -> dialog.cancel() })

        return builder.create()
    }
}
