package com.example.tetris.screens.gameoverscreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tetris.R
import com.example.tetris.RESULT_SCORE
import com.example.tetris.databinding.GameOverFragmentBinding

class GameOverFragment : Fragment() {

    private lateinit var binding: GameOverFragmentBinding
    private var gameResult = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_over_fragment,
            container,
            false
        )

        gameResult = arguments?.getInt(RESULT_SCORE) ?: 0
        binding.resultScore.text = gameResult.toString()

        binding.newGameButtonGameOverFragment.setOnClickListener {
            it.findNavController().navigate(R.id.action_gameOver_to_tetrisGameFragment)
        }

        binding.shareResultButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            with(shareIntent){
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "${getString(R.string.share_result_intent)} $gameResult \n" +
                        "Check for latest versions: ${getString(R.string.app_link)}")
            }
            startActivity(shareIntent)
        }

        return binding.root
    }
}