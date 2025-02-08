package com.example.familyapp.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import java.text.SimpleDateFormat
import java.util.*

class AddUserDialogFragment(private val onUserAdded: (User) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())

         val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_user, null)
        builder.setView(view)

        val etNom = view.findViewById<EditText>(R.id.et_nom)
        val etPrenom = view.findViewById<EditText>(R.id.et_prenom)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etProfession = view.findViewById<EditText>(R.id.et_profession)
        val etNumTel = view.findViewById<EditText>(R.id.et_num_tel)
        val etRole = view.findViewById<EditText>(R.id.et_role)

        view.findViewById<View>(R.id.btn_add_user).setOnClickListener {
            // Get the values from the fields
            val nom = etNom.text.toString()
            val prenom = etPrenom.text.toString()
            val email = etEmail.text.toString()
            val profession = etProfession.text.toString()
            val numTel = etNumTel.text.toString()
            val role = etRole.text.toString()

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || profession.isEmpty() || numTel.isEmpty() || role.isEmpty()) {
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

             val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val user = User(
                id = Random().nextInt(1000),
                nom = nom,
                prenom = prenom,
                email = email,
                motDePasse = "default_password",
                numTel = numTel,
                role = role,
                idFamille = 1,
                dateInscription = currentDate,
                coins = 0,
                avatar = "",
                totalPoints = 1,
                listOf()
            )

             onUserAdded(user)
            dismiss()
        }

        return builder.create()
    }
}
