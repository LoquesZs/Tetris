package com.example.tetris

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_game_over.*


class GameOver : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_game_over, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameResult = arguments?.getInt("result")
        result_score.text =  gameResult.toString()

        new_gama_button_game_over_fragment.setOnClickListener {
            it.findNavController().navigate(R.id.action_gameOver_to_tetrisFieldFragment)
        }

        share_result_button.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            with(shareIntent){
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "${getString(R.string.share_result_intent)} $gameResult \n" +
                        "Check for latest versions: ${getString(R.string.app_link)}")
            }

            startActivity(shareIntent)
        }
    }
}