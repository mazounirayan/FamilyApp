package com.example.familyapp.views.fragments.settings


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.familyapp.R

class ShareCodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inviteCode = view.findViewById<TextView>(R.id.tvInviteCode)
        val btnCopy = view.findViewById<Button>(R.id.btnCopy)
        val btnShare = view.findViewById<Button>(R.id.btnShare)

        val code = generateInviteCode()
        inviteCode.text = code

        btnCopy.setOnClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Invite Code", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Code copié !", Toast.LENGTH_SHORT).show()
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Voici mon code d'invitation : $code")
            }
            startActivity(Intent.createChooser(shareIntent, "Partager via"))
        }
    }

    private fun generateInviteCode(): String {
        return "ABC123XYZ"
    }
}