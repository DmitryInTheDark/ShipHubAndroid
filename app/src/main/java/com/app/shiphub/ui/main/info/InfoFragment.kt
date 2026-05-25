package com.app.shiphub.ui.main.info

import android.view.View
import com.app.shiphub.databinding.ItemInfoQuestionBinding
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.isVisible

@AndroidEntryPoint
class InfoFragment: BaseFragment<FragmentInfoBinding, InfoState, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModels()
    private val questionsMap = mapOf(
        R.string.question_1 to R.string.response_1,
        R.string.question_2 to R.string.response_2,
        R.string.question_3 to R.string.response_3,
        R.string.question_4 to R.string.response_4,
        R.string.question_5 to R.string.response_5,
        R.string.question_6 to R.string.response_6,
        R.string.question_7 to R.string.response_7,
        R.string.question_8 to R.string.response_8,
        R.string.question_9 to R.string.response_9,
        R.string.question_10 to R.string.response_10,
        R.string.question_11 to R.string.response_11,
        R.string.question_12 to R.string.response_12,
    )

    override fun initializeBinding() = FragmentInfoBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {
        questionsMap.forEach { (questionRes, responseRes) ->
            val itemBinding = ItemInfoQuestionBinding.inflate(layoutInflater, binding.llQuestions, false)
            itemBinding.tvQuestion.setText(questionRes)
            itemBinding.tvAnswer.setText(responseRes)

            itemBinding.cardQuestion.setOnClickListener {
                val isVisible = itemBinding.tvAnswer.isVisible
                itemBinding.tvAnswer.visibility = if (isVisible) View.GONE else View.VISIBLE
                itemBinding.ivArrow.animate().rotation(if (isVisible) 0f else 180f).start()
            }

            binding.llQuestions.addView(itemBinding.root)
        }
    }

    override fun handleState(state: InfoState) {

    }
}