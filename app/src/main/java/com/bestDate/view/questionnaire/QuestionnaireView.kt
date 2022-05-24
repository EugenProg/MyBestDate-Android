package com.bestDate.view.questionnaire

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.setupOnListener
import com.bestDate.databinding.PageQuestionnaireQuestionsBinding
import com.bestDate.databinding.PageQuestionnaireTextBinding
import com.bestDate.databinding.ViewQuestionnaireBinding
import com.hadilq.liveevent.LiveEvent

class QuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireBinding
    private var firstPage: PageQuestionnaireQuestionsBinding
    private var secondPage: PageQuestionnaireQuestionsBinding
    private var thirdPage: PageQuestionnaireQuestionsBinding
    private var forthPage: PageQuestionnaireQuestionsBinding
    private var fifthPage: PageQuestionnaireTextBinding
    private var sixthPage: PageQuestionnaireQuestionsBinding
    private var inProcess: Boolean = false

    private var totalPages: Int = 6
    private var currentPage: QuestionnairePage? = null
    private var pages: MutableList<QuestionnairePage> = ArrayList()
    var progressAdded: ((Int) -> Unit)? = null
    var collapseAction: ((Boolean) -> Unit)? = null
    var keyboardHideAction: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_questionnaire, this)
        binding = ViewQuestionnaireBinding.bind(view)

        firstPage = binding.firstPage
        secondPage = binding.secondPage
        thirdPage = binding.thirdPage
        forthPage = binding.fourthPage
        fifthPage = binding.fifthPage
        sixthPage = binding.sixthPage

        firstPage.nextButton.onClick = {
            currentPage = pages[1]
            setNextPage(firstPage.root, secondPage.root)
        }

        secondPage.nextButton.onClick = {
            currentPage = pages[2]
            setNextPage(secondPage.root, thirdPage.root)
        }
        secondPage.backButton.setOnSaveClickListener {
            currentPage = pages[0]
            setPreviousPage(secondPage.root, firstPage.root)
        }

        thirdPage.nextButton.onClick = {
            currentPage = pages[3]
            setNextPage(thirdPage.root, forthPage.root)
        }
        thirdPage.backButton.setOnSaveClickListener {
            currentPage = pages[1]
            setPreviousPage(thirdPage.root, secondPage.root)
        }

        forthPage.nextButton.onClick = {
            currentPage = pages[4]
            setNextPage(forthPage.root, fifthPage.root)
        }
        forthPage.backButton.setOnSaveClickListener {
            currentPage = pages[2]
            setPreviousPage(forthPage.root, thirdPage.root)
        }

        fifthPage.nextButton.onClick = {
            currentPage = pages[5]
            setNextPage(fifthPage.root, sixthPage.root)
        }
        fifthPage.backButton.setOnSaveClickListener {
            currentPage = pages[3]
            setPreviousPage(fifthPage.root, forthPage.root)
        }

        sixthPage.nextButton.onClick = { }
        sixthPage.backButton.setOnSaveClickListener {
            currentPage = pages[4]
            setPreviousPage(sixthPage.root, fifthPage.root)
        }
    }

    fun setPages(pageList: MutableList<QuestionnairePage>) {
        pages.clear()
        pages.addAll(pageList)
        totalPages = pageList.size

        for (page in pageList) {
            when(page.number) {
                1 -> {
                    currentPage = page
                    setPage(page, firstPage)
                }
                2 -> setPage(page, secondPage)
                3 -> setPage(page, thirdPage)
                4 -> setPage(page, forthPage)
                5 -> setPage(page, fifthPage)
                6 -> setPage(page, sixthPage)
            }
        }
    }

    private fun setPage(page: QuestionnairePage, binding: ViewBinding) {
        when (page.type) {
            QuestionnairePageType.QUESTION_LIST -> {
                setQuestionnairePage(page, binding as PageQuestionnaireQuestionsBinding)
            }
            QuestionnairePageType.MULTILINE_INPUT -> {
                setTextPage(page, binding as PageQuestionnaireTextBinding)
            }
        }

    }

    private fun setQuestionnairePage(page: QuestionnairePage, binding: PageQuestionnaireQuestionsBinding) {
        with(binding) {
            nextButton.title = context.getString(page.nextButtonText)

            header.currentPageNumber.text = page.number.toString()
            header.totalPagesNumber.text = totalPages.toString()
            header.title.text = context.getString(page.title)

            if (page.number == 1) {
                backButton.isEnabled = false
                backButton.setTextColor(ContextCompat.getColor(context, R.color.main_10))
            }
        }
    }

    private fun setTextPage(page: QuestionnairePage, binding: PageQuestionnaireTextBinding) {
        with(binding) {
            nextButton.title = context.getString(R.string.next)

            header.currentPageNumber.text = page.number.toString()
            header.totalPagesNumber.text = totalPages.toString()
            header.title.text = context.getString(page.title)
        }
    }

    private fun saveChanges() {

    }

    private fun setNextPage(topView: View, bottomView: View) {
        if (inProcess) return
        collapseAction?.invoke(false)
        keyboardHideAction?.invoke()
        inProcess = true
        bottomView.animate()
            .setDuration(1)
            .alpha(0.0f)
            .translationY(100f)
            .scaleX(0.9f)
            .start()

        bottomView.isVisible = true
        bottomView.alpha = 0.0f

        val animator = AnimatorSet()
        val topRotation = ObjectAnimator.ofFloat(topView, View.ROTATION, 0f, -30f)
        val topMoveBottom = ObjectAnimator.ofFloat(topView, View.TRANSLATION_Y, 360f)
        val topMoveLeft = ObjectAnimator.ofFloat(topView, View.TRANSLATION_X, (-(topView.width * 1.2)).toFloat())
        val bottomTranslation = ObjectAnimator.ofFloat(bottomView, View.TRANSLATION_Y, 0f)
        val bottomScale = ObjectAnimator.ofFloat(bottomView, View.SCALE_X, 1f)
        val bottomAlpha = ObjectAnimator.ofFloat(bottomView, View.ALPHA, 0f, 1f)

        animator.startDelay = 100
        animator.duration = 400
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.play(topMoveBottom).with(topMoveLeft).with(topRotation)
            .with(bottomAlpha).with(bottomTranslation).with(bottomScale)
        animator.start()

        animator.setupOnListener(end = {
            topView.isVisible = false
            inProcess = false
        })
    }

    private fun setPreviousPage(bottomView: View, topView: View) {
        if (inProcess) return
        collapseAction?.invoke(false)
        keyboardHideAction?.invoke()
        inProcess = true
        topView.isVisible = true

        val animator = AnimatorSet()
        val topRotation = ObjectAnimator.ofFloat(topView, View.ROTATION, -30f, 0f)
        val topMoveBottom = ObjectAnimator.ofFloat(topView, View.TRANSLATION_Y, 0f)
        val topMoveLeft = ObjectAnimator.ofFloat(topView, View.TRANSLATION_X, 0f)
        val bottomTranslation = ObjectAnimator.ofFloat(bottomView, View.TRANSLATION_Y, 100f)
        val bottomScale = ObjectAnimator.ofFloat(bottomView, View.SCALE_X, 0.9f)
        val bottomAlpha = ObjectAnimator.ofFloat(bottomView, View.ALPHA, 1f, 0f)

        animator.startDelay = 100
        animator.duration = 400
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.play(topMoveBottom).with(topMoveLeft).with(topRotation)
            .with(bottomAlpha).with(bottomTranslation).with(bottomScale)
        animator.start()

        animator.setupOnListener(end = {
            bottomView.isVisible = false
            inProcess = false
        })
    }
}