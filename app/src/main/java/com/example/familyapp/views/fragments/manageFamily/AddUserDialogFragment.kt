package com.example.familyapp.views



import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.familyapp.R
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.User
import java.util.*

class AddUserDialogFragment(private val onUserAdded: (AddUserRequest) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_user, null)
        builder.setView(view)

        val etNom = view.findViewById<EditText>(R.id.et_nom)
        val etPrenom = view.findViewById<EditText>(R.id.et_prenom)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val spinnerRole = view.findViewById<Spinner>(R.id.spinner_role)

        view.findViewById<Button>(R.id.btn_add_user).setOnClickListener {
            val nom = etNom.text.toString().trim()
            val prenom = etPrenom.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val role = spinnerRole.selectedItem.toString()

             val addUserRequest = AddUserRequest(
                nom = nom,
                prenom = prenom,
                email = email,
                motDePasse = "$prenom${Random().nextInt(999)}",
                role = role
            )

            onUserAdded(addUserRequest)
            dismiss()
        }

        return builder.create()
    }
}
