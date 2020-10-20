package com.example.tetris.screens.gamescreen

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tetris.R
import com.example.tetris.RESULT_SCORE
import com.example.tetris.databinding.TetrisGameFragmentBinding
import com.example.tetris.utils.ScoreHolder
import kotlinx.android.synthetic.main.tetris_game_fragment.*

class TetrisGameFragment : Fragment() {

    private lateinit var binding: TetrisGameFragmentBinding
    private lateinit var tetrisGameViewModel: TetrisGameViewModel
    private lateinit var tetrisGameViewModelFactory: TetrisGameViewModelFactory
    private lateinit var scoreHolder: ScoreHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.tetris_game_fragment,
            container,
            false
        )

        scoreHolder = activity?.let { ScoreHolder(it) }!!
        tetrisGameViewModelFactory = TetrisGameViewModelFactory(scoreHolder)
        tetrisGameViewModel = ViewModelProvider(this, tetrisGameViewModelFactory).get(
            TetrisGameViewModel::class.java
        )

        tetrisGameViewModel.isGameOver.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.tetrisGameFragment) {
                val bundle = bundleOf(RESULT_SCORE to tetrisGameViewModel.score.value)
                navController.navigate(R.id.action_tetrisGameFragment_to_gameOver, bundle)
            }
        })

        tetrisGameViewModel.gameField.observe(viewLifecycleOwner, Observer {
            tetrisField.invalidate()
        })

        tetrisGameViewModel.score.observe(viewLifecycleOwner, Observer {
            binding.scoreDisplay.text = it.toString()
        })

        binding.bestScoreDisplay.text = scoreHolder.bestScore.toString()

        binding.leftButton.setOnClickListener {
            tetrisGameViewModel.moveLeft()
        }
        binding.rotateButton.setOnClickListener {
            tetrisGameViewModel.rotateFigure()
        }
        binding.rightButton.setOnClickListener {
            tetrisGameViewModel.moveRight()
        }

        binding.speedUpButton.setOnTouchListener { v, event ->
            v.performClick()
            if (event != null) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        tetrisGameViewModel.speedUp()
                    }
                    MotionEvent.ACTION_UP -> {
                        tetrisGameViewModel.slowDown()
                    }
                }
            }
            true
        }

        binding.newGameButton.setOnClickListener {
            tetrisGameViewModel.endGame()
            scoreHolder.saveBestScore()
            scoreHolder.currentScore = 0
            score_display.text = scoreHolder.currentScore.toString()
            tetrisGameViewModel.startGame()
        }

        binding.pauseButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_tetrisGameFragment_to_pauseMenu)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tetrisField.field = tetrisGameViewModel.gameField.value!!
    }

    override fun onStart() {
        tetrisGameViewModel.startGame()
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        tetrisGameViewModel.pause()
        scoreHolder.saveBestScore()
    }

    override fun onResume() {
        super.onResume()
        tetrisGameViewModel.resume()
    }

    override fun onStop() {
        super.onStop()
        tetrisGameViewModel.endGame()
    }
}