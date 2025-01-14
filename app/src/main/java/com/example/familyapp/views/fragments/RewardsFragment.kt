import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.user.User
import com.example.familyapp.viewmodel.RecompenseViewModel
import com.example.familyapp.views.recycler_view_adapters.recompense_adapters.UserRankAdapter


class RewardsFragment : Fragment() {
    private lateinit var userAdapter: UserRankAdapter
    private lateinit var recompenseAdapter: RecompenseAdapter
    private val viewModel: RecompenseViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recompense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeData()
    }

    private fun setupRecyclerViews() {
        userAdapter = UserRankAdapter()
        recompenseAdapter = RecompenseAdapter()

        val rvUserRanking = view?.findViewById<RecyclerView>(R.id.rvUserRanking)
        rvUserRanking?.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val rvRecompenses = view?.findViewById<RecyclerView>(R.id.rvRecompenses)
        rvRecompenses?.apply {
            adapter = recompenseAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun observeData() {
        // Données de test
        val mockUsers = listOf(
            User(1, "Martin", "Sophie", "sophie@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  1500, "avatar1.jpg",2000),
            User(2, "Martin", "Lucas", "lucas@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  1200, "avatar2.jpg",1800),
            User(3, "Martin", "Emma", "emma@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  800, "avatar3.jpg",1200)
        )

        val mockRecompenses = listOf(
            Recompense(1, "Nintendo Switch", "1 heure de jeu", 100, 5, true),
            Recompense(2, "Sortie cinéma", "Une séance au choix", 200, 3, true),
            Recompense(3, "Restaurant", "Menu enfant", 150, 2, true),
            Recompense(4, "Parc d'attractions", "Une journée", 500, 1, true)
        )

        userAdapter.submitList(mockUsers.sortedByDescending { it.coins })
        recompenseAdapter.submitList(mockRecompenses)

        // Mettre à jour les points de l'utilisateur actuel
        view?.findViewById<TextView>(R.id.tvPoints)?.text = "1500"
    }
}



