package com.example.tetris

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_nav_host.*

class NavHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_host,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(activity!!.getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
            lightAppTheme -> switch_theme_button.isChecked = false
            darkAppTheme -> switch_theme_button.isChecked = true
        }
        start_button.setOnClickListener {
            it.findNavController().navigate(R.id.action_navHostFragment_to_tetrisFieldFragment)
        }
        switch_theme_button.setOnClickListener {
            when (activity!!.getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
                lightAppTheme -> activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString(appThemeStorage, darkAppTheme).apply()
                darkAppTheme -> activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString(appThemeStorage, lightAppTheme).apply()
            }
            val appIntent = activity!!.intent
            activity!!.finish()
            startActivity(appIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        best_score_display_start_menu.text = sharedPreferences.getString("Best Score Storage", "0")
    }
}