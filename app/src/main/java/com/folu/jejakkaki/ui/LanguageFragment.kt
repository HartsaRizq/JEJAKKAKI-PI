package com.folu.jejakkaki.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.folu.jejakkaki.R
import com.yariksoffice.lingver.Lingver

class LanguageFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View =  inflater.inflate(R.layout.fragment_language, container, false)
        val btnID = rootView.findViewById<Button>(R.id.btnID)
        val btnEN = rootView.findViewById<Button>(R.id.btnENG)
        var change = false
        btnID.setOnClickListener {
            Lingver.getInstance().setLocale(requireActivity(), "in")
            saveSelectedLanguage("in", !change)
            dismiss()
            requireActivity().recreate()
            showToast(getString(R.string.ganti_bahasa))
        }

        btnEN.setOnClickListener {
            Lingver.getInstance().setLocale(requireActivity(), "en")
            saveSelectedLanguage("en", !change)
            dismiss()
            requireActivity().recreate()
            showToast(getString(R.string.ganti_bahasa))
        }


        return rootView
    }

    private fun saveSelectedLanguage(language: String, change: Boolean) {
        // You can use SharedPreferences to save the selected language
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selectedLanguage", language)
        editor.putBoolean("changedLanguage", change)
        editor.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
