import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.familyapp.R

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Lier le layout au fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Récupérer les composants du layout
        val emailInput = view.findViewById<EditText>(R.id.email_input)
        val passwordInput = view.findViewById<EditText>(R.id.password_input)
        val loginButton = view.findViewById<Button>(R.id.login_button)

        // Ajouter un listener au bouton Log In
        loginButton.setOnClickListener {
            // Récupérer les entrées utilisateur
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Valider les entrées utilisateur
            if (validateInput(email, password)) {
                // Appeler la logique de connexion ou navigation
                handleLogin(email, password)
            }
        }

        return view
    }

    // Validation des champs de saisie
    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                Toast.makeText(context, "Email ne peut pas être vide", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Mot de passe ne peut pas être vide", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(context, "Email invalide", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    // Logique à exécuter après la validation
    private fun handleLogin(email: String, password: String) {
        // Exemple de logique pour la connexion
        if (email == "test@example.com" && password == "password123") {
            // Afficher un succès
            Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
            // Naviguer vers une autre page (par exemple HomeFragment)
            navigateToHome()
        } else {
            // Afficher un message d'erreur
            Toast.makeText(context, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
        }
    }

    // Naviguer vers un autre fragment
    private fun navigateToHome() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.viewPager, HomeFragment()) // Assure-toi que le container est défini dans ton activité
            .addToBackStack(null) // Ajoute à la pile pour permettre de revenir en arrière
            .commit()
    }
}
