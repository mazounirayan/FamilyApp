import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.user.User
import com.example.familyapp.repositories.RewardsRepository
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.viewmodel.RewardsViewModel
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.factories.RewardsViewModelFactory
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.views.fragments.dialogueRecompens.AjoutRecompenseDialog
import com.example.familyapp.views.fragments.dialogueRecompens.ModifierRecompenseDialog
import com.example.familyapp.views.recycler_view_adapters.recompense_adapters.UserRankAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RewardsFragment : Fragment(), AjoutRecompenseDialog.OnRecompenseAjouteeListener ,ModifierRecompenseDialog.OnRecompenseModifieeListener {
    private lateinit var userAdapter: UserRankAdapter
    private lateinit var recompenseAdapter: RecompenseAdapter
    private val rewardsViewModel: RewardsViewModel by viewModels {
        RewardsViewModelFactory(
            RewardsRepository(this.requireContext()),
            UserRepository(this.requireContext()),this

        )
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recompense, container, false)
    }
    override fun onRecompenseAjoutee(nom: String, description: String, cout: Int, stock: Int,estDisponible:Boolean) {
        rewardsViewModel.ajouterRecompense(nom, description, cout, stock,estDisponible)
        fetchData()
    }
    override fun onRecompenseModifiee(idRecompense:Int , nom: String, description: String, cout: Int, stock: Int,estDisponible:Boolean) {
        rewardsViewModel.updateRecompense(idRecompense,nom, description, cout, stock,estDisponible)
        fetchData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeData()
        fetchData()


        view.findViewById<FloatingActionButton>(R.id.fabAddRecompense)?.apply {
            visibility = if (rewardsViewModel.currentUser.value?.role == "PARENT") View.VISIBLE else View.GONE
            setOnClickListener {
                showAjoutDialog()
            }
        }


    }
    private fun showAjoutDialog() {
        AjoutRecompenseDialog().show(childFragmentManager, "ajout_recompense")

    }
    private fun setupRecyclerViews() {
        val isParent = rewardsViewModel.currentUser.value?.role == "PARENT"

        userAdapter = UserRankAdapter()
        recompenseAdapter = RecompenseAdapter(
            isParent = isParent,
            onModifierClick = { recompense ->
                if (isParent) {
                    showModifierDialog(recompense)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Seuls les parents peuvent modifier les récompenses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onSupprimerClick = { recompense ->
                if (isParent) {
                    showConfirmationDialog(recompense)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Seuls les parents peuvent supprimer les récompenses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onEchangerClick = { recompense ->
                if (!isParent) {
                    val currentUser = rewardsViewModel.currentUser.value
                    if (currentUser != null && currentUser.coins >= recompense.cout) {
                        rewardsViewModel.echangerRecompense(recompense.idRecompense)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Pas assez de points pour échanger cette récompense",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Seuls les enfants peuvent échanger des récompenses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        // Configuration du RecyclerView pour le classement des utilisateurs
        view?.findViewById<RecyclerView>(R.id.rvUserRanking)?.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Configuration du RecyclerView pour les récompenses
        view?.findViewById<RecyclerView>(R.id.rvRecompenses)?.apply {
            adapter = recompenseAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun showModifierDialog(recompense: Recompense) {
        ModifierRecompenseDialog.newInstance(recompense)
            .show(childFragmentManager, "modifier_recompense")
    }

    private fun showConfirmationDialog(recompense: Recompense) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Voulez-vous vraiment supprimer cette récompense ?")
            .setPositiveButton("Oui") { _, _ ->
                rewardsViewModel.supprimerRecompense(recompense.idRecompense)
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun observeData() {
        rewardsViewModel.users.observe(viewLifecycleOwner) { users ->
            users?.let {
                userAdapter.submitList(it.sortedByDescending { user -> user.coins })
            }
        }

        rewardsViewModel.recompenses.observe(viewLifecycleOwner, Observer { recompenses ->
            recompenses?.let {
                recompenseAdapter.submitList(it)
            }
        })
        rewardsViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            currentUser?.let {
                view?.findViewById<TextView>(R.id.tvPoints)?.text = it.coins.toString()
            }
        }



    }
    private fun fetchData() {
        // Récupérer les récompenses et les membres
        val familyId = 1 // Remplacez par l'ID de famille réel
        rewardsViewModel.fetchRecompense(familyId)
        rewardsViewModel.fetchMembers(familyId)
    }



}



