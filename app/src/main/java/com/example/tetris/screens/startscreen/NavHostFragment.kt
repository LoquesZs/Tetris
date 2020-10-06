package com.example.tetris.screens.startscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.tetris.R
import com.example.tetris.APP_THEME_STORAGE
import com.example.tetris.DARK_APP_THEME
import com.example.tetris.LIGHT_APP_THEME
import com.example.tetris.utils.ScoreHolder
import kotlinx.android.synthetic.main.fragment_nav_host.*

class NavHostFragment : Fragment() {

    private lateinit var scoreHolder: ScoreHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scoreHolder = activity?.let { ScoreHolder(it) }!!
        return inflater.inflate(R.layout.fragment_nav_host,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(activity!!.getPreferences(Context.MODE_PRIVATE).getString(APP_THEME_STORAGE, LIGHT_APP_THEME)) {
            LIGHT_APP_THEME -> switch_theme_button.isChecked = false
            DARK_APP_THEME -> switch_theme_button.isChecked = true
        }
        start_button.setOnClickListener {
            it.findNavController().navigate(R.id.action_navHostFragment_to_tetrisGameFragment)
        }
        switch_theme_button.setOnClickListener {
            when (activity!!.getPreferences(Context.MODE_PRIVATE).getString(APP_THEME_STORAGE, LIGHT_APP_THEME)) {
                LIGHT_APP_THEME -> activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString(
                    APP_THEME_STORAGE, DARK_APP_THEME
                ).apply()
                DARK_APP_THEME -> activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString(
                    APP_THEME_STORAGE, LIGHT_APP_THEME
                ).apply()
            }
            val appIntent = activity!!.intent
            activity!!.finish()
            startActivity(appIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        best_score_display_start_menu.text = scoreHolder.bestScore.toString()
    }


}