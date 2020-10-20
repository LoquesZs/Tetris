package com.example.tetris.screens.startscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tetris.R
import com.example.tetris.APP_THEME_STORAGE
import com.example.tetris.DARK_APP_THEME
import com.example.tetris.LIGHT_APP_THEME
import com.example.tetris.databinding.FragmentNavHostBinding
import com.example.tetris.utils.ScoreHolder
import kotlinx.android.synthetic.main.fragment_nav_host.*

class NavHostFragment : Fragment() {

    private lateinit var scoreHolder: ScoreHolder
    private lateinit var binding: FragmentNavHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_nav_host,
            container,
            false
        )

        scoreHolder = activity?.let { ScoreHolder(it) }!!

        binding.startButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_navHostFragment_to_tetrisGameFragment)
        }
        binding.switchThemeButton.setOnClickListener {
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(activity!!.getPreferences(Context.MODE_PRIVATE).getString(APP_THEME_STORAGE, LIGHT_APP_THEME)) {
            LIGHT_APP_THEME -> switch_theme_button.isChecked = false
            DARK_APP_THEME -> switch_theme_button.isChecked = true
        }
    }

    override fun onStart() {
        super.onStart()
        binding.bestScoreDisplayStartMenu.text = scoreHolder.bestScore.toString()
    }
}