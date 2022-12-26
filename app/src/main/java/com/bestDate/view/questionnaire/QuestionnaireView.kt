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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.PageQuestionnaireQuestionsBinding
import com.bestDate.databinding.PageQuestionnaireTextBinding
import com.bestDate.databinding.ViewQuestionnaireBinding
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.presentation.base.questionnaire.*
import com.bestDate.view.questionnaire.list.QuestionnaireListAdapter

class QuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireBinding
    private lateinit var personalPage: PageQuestionnaireQuestionsBinding
    private lateinit var personalPageAdapter: QuestionnaireListAdapter
    private var personalPageQuestionsList = MutableLiveData<MutableList<QuestionnaireQuestion>>()

    private lateinit var appearancePage: PageQuestionnaireQuestionsBinding
    private lateinit var appearancePageAdapter: QuestionnaireListAdapter
    private var appearancePageQuestionsList = MutableLiveData<MutableList<QuestionnaireQuestion>>()

    private lateinit var searchPage: PageQuestionnaireQuestionsBinding
    private lateinit var searchPageAdapter: QuestionnaireListAdapter
    private var searchPageQuestionsList = MutableLiveData<MutableList<QuestionnaireQuestion>>()

    private lateinit var freeTimePage: PageQuestionnaireQuestionsBinding
    private lateinit var freeTimePageAdapter: QuestionnaireListAdapter
    private var freeTimePageQuestionsList = MutableLiveData<MutableList<QuestionnaireQuestion>>()

    private lateinit var aboutMePage: PageQuestionnaireTextBinding

    private lateinit var dataPage: PageQuestionnaireQuestionsBinding
    private lateinit var dataPageAdapter: QuestionnaireListAdapter
    private var dataPageQuestionsList = MutableLiveData<MutableList<QuestionnaireQuestion>>()

    private var animationInProcess: Boolean = false

    private var viewsStack: MutableList<View> = ArrayList()
    private var aboutMe: String? = null

    private var totalPages: Int = 6
    private var pages: MutableList<QuestionnairePage> = ArrayList()
    var progressAdded: ((Int) -> Unit)? = null
    var collapseAction: ((Boolean) -> Unit)? = null
    var keyboardHideAction: (() -> Unit)? = null
    var questionClick: ((QuestionnaireQuestion, MutableLiveData<MutableList<QuestionnaireQuestion>>) -> Unit)? =
        null
    var finishClick: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_questionnaire, this)
        binding = ViewQuestionnaireBinding.bind(view)

        bindView()
    }

    private fun bindView() {
        personalPage = binding.firstPage
        personalPageAdapter =
            QuestionnaireListAdapter { questionClick?.invoke(it, personalPageQuestionsList) }

        appearancePage = binding.secondPage
        appearancePageAdapter =
            QuestionnaireListAdapter { questionClick?.invoke(it, appearancePageQuestionsList) }

        searchPage = binding.thirdPage
        searchPageAdapter =
            QuestionnaireListAdapter { questionClick?.invoke(it, searchPageQuestionsList) }

        freeTimePage = binding.fourthPage
        freeTimePageAdapter =
            QuestionnaireListAdapter { questionClick?.invoke(it, freeTimePageQuestionsList) }

        aboutMePage = binding.fifthPage

        dataPage = binding.sixthPage
        dataPageAdapter =
            QuestionnaireListAdapter { questionClick?.invoke(it, dataPageQuestionsList) }

        viewClickListeners()
    }

    private fun viewClickListeners() {
        personalPage.nextButton.onClick = { toNextPage(personalPage.root, appearancePage.root) }

        appearancePage.nextButton.onClick = { toNextPage(appearancePage.root, searchPage.root) }
        appearancePage.previousButton.setOnSaveClickListener {
            toPreviousPage(appearancePage.root, personalPage.root)
        }

        searchPage.nextButton.onClick = { toNextPage(searchPage.root, freeTimePage.root) }
        searchPage.previousButton.setOnSaveClickListener {
            toPreviousPage(searchPage.root, appearancePage.root)
        }

        freeTimePage.nextButton.onClick = { toNextPage(freeTimePage.root, aboutMePage.root) }
        freeTimePage.previousButton.setOnSaveClickListener {
            toPreviousPage(freeTimePage.root, searchPage.root)
        }

        aboutMePage.nextButton.onClick = {
            checkFilling(aboutMePage)
            toNextPage(aboutMePage.root, dataPage.root)
        }
        aboutMePage.previousButton.setOnSaveClickListener {
            checkFilling(aboutMePage)
            toPreviousPage(aboutMePage.root, freeTimePage.root)
        }
        aboutMePage.textInput.textIsChanged { checkFilling(aboutMePage) }

        dataPage.nextButton.onClick = {
            finishClick?.invoke()
        }
        dataPage.previousButton.setOnSaveClickListener {
            toPreviousPage(dataPage.root, aboutMePage.root)
        }
    }

    fun goBack(): Boolean {
        if (viewsStack.size > 1) toPreviousPage(
            viewsStack.last(),
            viewsStack[viewsStack.lastIndex - 1]
        )
        else if (viewsStack.size == 1) toPreviousPage(appearancePage.root, personalPage.root)
        return viewsStack.isNotEmpty()
    }

    private fun checkFilling(textPage: PageQuestionnaireTextBinding) {
        val input = textPage.textInput.text
        when {
            aboutMe == null && !input.isNullOrBlank() -> {
                setTextPercentColor(true, textPage)
                progressAdded?.invoke(7)
            }
            aboutMe != null && input.isNullOrBlank() -> {
                setTextPercentColor(false, textPage)
                progressAdded?.invoke(-7)
            }
        }

        aboutMe = if (input.isNullOrBlank()) null
        else input.toString()
    }

    private fun setTextPercentColor(active: Boolean, textPage: PageQuestionnaireTextBinding) {
        val color =
            ContextCompat.getColor(context, if (active) R.color.blue_90 else R.color.main_30)
        textPage.percent.setTextColor(color)
        textPage.plus.setTextColor(color)
        textPage.percentNumber.setTextColor(color)
    }

    fun viewLifecycle(owner: LifecycleOwner) {
        personalPageQuestionsList.observe(owner) {
            personalPageAdapter.submitList(it)
        }
        appearancePageQuestionsList.observe(owner) {
            appearancePageAdapter.submitList(it)
        }
        searchPageQuestionsList.observe(owner) {
            searchPageAdapter.submitList(it)
        }
        freeTimePageQuestionsList.observe(owner) {
            freeTimePageAdapter.submitList(it)
        }
        dataPageQuestionsList.observe(owner) {
            dataPageAdapter.submitList(it)
        }
    }

    fun setPages(pageList: MutableList<QuestionnairePage>) {
        pages.clear()
        pages.addAll(pageList)
        totalPages = pageList.size

        for (page in pageList) {
            when (page.number) {
                1 -> setPage(page, personalPage, personalPageAdapter, personalPageQuestionsList)
                2 -> setPage(
                    page,
                    appearancePage,
                    appearancePageAdapter,
                    appearancePageQuestionsList
                )
                3 -> setPage(page, searchPage, searchPageAdapter, searchPageQuestionsList)
                4 -> setPage(page, freeTimePage, freeTimePageAdapter, freeTimePageQuestionsList)
                5 -> setPage(page, aboutMePage)
                6 -> setPage(page, dataPage, dataPageAdapter, dataPageQuestionsList)
            }
        }
    }

    private fun setPage(
        page: QuestionnairePage,
        binding: ViewBinding,
        adapter: QuestionnaireListAdapter? = null,
        list: MutableLiveData<MutableList<QuestionnaireQuestion>>? = null
    ) {
        when (page.type) {
            QuestionnairePageType.QUESTION_LIST -> {
                setQuestionnairePage(page, binding as PageQuestionnaireQuestionsBinding, adapter)
                list?.value = page.questions
            }
            QuestionnairePageType.MULTILINE_INPUT -> {
                setTextPage(page, binding as PageQuestionnaireTextBinding)
            }
        }
    }

    private fun setQuestionnairePage(
        page: QuestionnairePage,
        binding: PageQuestionnaireQuestionsBinding,
        adapter: QuestionnaireListAdapter? = null
    ) {
        with(binding) {
            nextButton.title = context.getString(page.nextButtonText)

            header.currentPageNumber.text = page.number.toString()
            header.totalPagesNumber.text = totalPages.toString()
            header.title.text = context.getString(page.title)

            questionsList.layoutManager = LinearLayoutManager(context)
            questionsList.adapter = adapter

            if (page.number == 1) {
                previousButton.isEnabled = false
                previousButton.setTextColor(ContextCompat.getColor(context, R.color.main_10))
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

    fun updateQuestionnaireList(
        question: QuestionnaireQuestion,
        answer: String,
        list: MutableLiveData<MutableList<QuestionnaireQuestion>>
    ) {
        val items: MutableList<QuestionnaireQuestion> = ArrayList()

        for (item in list.value ?: ArrayList()) {
            if (item.questionInfo == question.questionInfo) {
                if (item.answer.isNullOrBlank()) progressAdded?.invoke(item.questionInfo?.percent.orZero)
                items.add(QuestionnaireQuestion(item.questionInfo, answer))
            } else {
                items.add(QuestionnaireQuestion(item.questionInfo, item.answer))
            }
        }

        list.value = items
    }

    fun setQuestionnaire(questionnaire: QuestionnaireDB?, email: String?, phone: String?, photos: Int) {
        if (questionnaire == null) return
        var progress = 0
        //Personal
        val personalItems: MutableList<QuestionnaireQuestion> = mutableListOf()
        for (item in personalPageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.MARITAL_STATUS -> personalItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        MaritalStatus().getName(context, questionnaire.marital_status)
                    )
                )
                Question.HAVING_KIDS -> personalItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        KidsCount().getName(context, questionnaire.kids)
                    )
                )
                Question.PLACE_OF_RESIDENCE -> personalItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        NationalityType().getName(context, questionnaire.nationality)
                    )
                )
                Question.EDUCATION -> personalItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        EducationStatus().getName(context, questionnaire.education)
                    )
                )
                Question.OCCUPATIONAL_STATUS -> personalItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        OccupationalStatus().getName(context, questionnaire.occupation)
                    )
                )
            }
        }
        personalPageQuestionsList.value = personalItems
        for (item in personalItems) {
            if (item.answer?.isNotBlank() == true && item.answer != "0")
                progress += item.questionInfo?.percent.orZero
        }

        //Appearance
        val appearanceItems: MutableList<QuestionnaireQuestion> = mutableListOf()
        for (item in appearancePageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.HEIGHT -> appearanceItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        questionnaire.height.orZero.toString()
                    )
                )
                Question.WEIGHT -> appearanceItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        questionnaire.weight.orZero.toString()
                    )
                )
                Question.EYE_COLOR -> appearanceItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        EyeColorType().getName(context, questionnaire.eye_color)
                    )
                )
                Question.HAIR_LENGTH -> appearanceItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        HairLengthType().getName(context, questionnaire.hair_length)
                    )
                )
                Question.HAIR_COLOR -> appearanceItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        HairColorType().getName(context, questionnaire.hair_color)
                    )
                )
            }
        }
        appearancePageQuestionsList.value = appearanceItems
        for (item in appearanceItems) {
            if (item.answer?.isNotBlank() == true && item.answer != "0")
                progress += item.questionInfo?.percent.orZero
        }

        //Search
        val searchItems: MutableList<QuestionnaireQuestion> = mutableListOf()
        for (item in searchPageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.PURPOSE_OF_DATING -> searchItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        PurposeOfDating().getName(context, questionnaire.purpose)
                    )
                )
                Question.WHAT_DO_YOU_WANT -> searchItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        ExpectationType().getName(context, questionnaire.expectations)
                    )
                )
                Question.SEARCH_LOCATION -> searchItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        questionnaire.getLocation()
                    )
                )
                Question.AGE -> searchItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        questionnaire.getAgeRange()
                    )
                )
            }
        }
        searchPageQuestionsList.value = searchItems
        for (item in searchItems) {
            if (item.answer?.isNotBlank() == true && item.answer != "0")
                progress += item.questionInfo?.percent.orZero
        }

        //Free time
        val freeItems: MutableList<QuestionnaireQuestion> = mutableListOf()
        for (item in freeTimePageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.HOBBY -> freeItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        HobbyType().getNameLine(context, questionnaire.hobby)
                    )
                )
                Question.TYPES_OF_SPORTS -> freeItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        SportTypes().getNameLine(context, questionnaire.sport)
                    )
                )
                Question.EVENING_TYPE -> freeItems.add(
                    QuestionnaireQuestion(
                        item.questionInfo,
                        EveningTimeType().getName(context, questionnaire.evening_time)
                    )
                )
            }
        }
        freeTimePageQuestionsList.value = freeItems
        for (item in freeItems) {
            if (item.answer?.isNotBlank() == true && item.answer != "0")
                progress += item.questionInfo?.percent.orZero
        }

        //Data
        val dataItems: MutableList<QuestionnaireQuestion> = mutableListOf()
        dataPageQuestionsList.value?.forEach {
            when(it.questionInfo) {
                Question.PHOTO -> dataItems.add(
                    QuestionnaireQuestion(
                        it.questionInfo,
                        if (photos > 0) "photo" else null
                    )
                )
                Question.EMAIL -> dataItems.add(
                    QuestionnaireQuestion(
                        it.questionInfo,
                        email
                    )
                )
                Question.PHONE_NUMBER -> dataItems.add(
                    QuestionnaireQuestion(
                        it.questionInfo,
                        phone
                    )
                )
                Question.SOCIAL_NETWORK -> dataItems.add(
                    QuestionnaireQuestion(
                        it.questionInfo,
                        questionnaire.socials?.joinToString()
                    )
                )
            }
        }
        dataPageQuestionsList.value = dataItems

        //About me
        aboutMePage.textInput.setText(questionnaire.about_me)

        progressAdded?.invoke(progress)
    }

    fun toggleFinishButton(enable: Boolean) {
        dataPage.nextButton.toggleActionEnabled(enable)
    }

    private fun toNextPage(topView: View, bottomView: View) {
        if (animationInProcess) return
        collapseAction?.invoke(false)
        keyboardHideAction?.invoke()
        animationInProcess = true
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
        val topMoveLeft =
            ObjectAnimator.ofFloat(topView, View.TRANSLATION_X, (-(topView.width * 1.2)).toFloat())
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
            animationInProcess = false
        })

        if (!viewsStack.contains(topView)) viewsStack.add(topView)
        if (!viewsStack.contains(bottomView)) viewsStack.add(bottomView)
    }

    private fun toPreviousPage(bottomView: View, topView: View) {
        if (animationInProcess) return
        collapseAction?.invoke(false)
        keyboardHideAction?.invoke()
        animationInProcess = true
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
            animationInProcess = false
        })

        if (viewsStack.size == 1) viewsStack.remove(topView)
        else viewsStack.remove(bottomView)
    }

    fun setKeyboardAction(isVisible: Boolean) {
        aboutMePage.header.root.isVisible = !isVisible
        aboutMePage.percentContainer.isVisible = !isVisible
        aboutMePage.nextButton.isVisible = !isVisible
        aboutMePage.previousButton.isVisible = !isVisible
        if (isVisible) {
            aboutMePage.textInput.setSelection(aboutMePage.textInput.text.length)
            aboutMePage.textInput.requestFocus()
        }
    }

    fun getQuestionnaire(): QuestionnaireDB {
        val questionnaire = QuestionnaireDB()

        for (item in personalPageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.MARITAL_STATUS -> questionnaire.marital_status = MaritalStatus().getServerName(context, item.answer)
                Question.HAVING_KIDS -> questionnaire.kids = KidsCount().getServerName(context, item.answer)
                Question.PLACE_OF_RESIDENCE -> questionnaire.nationality = NationalityType().getServerName(context, item.answer)
                Question.EDUCATION -> questionnaire.education = EducationStatus().getServerName(context, item.answer)
                Question.OCCUPATIONAL_STATUS -> questionnaire.occupation = OccupationalStatus().getServerName(context, item.answer)
            }
        }

        //Appearance
        for (item in appearancePageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.HEIGHT -> questionnaire.height = item.answer?.toInt()
                Question.WEIGHT -> questionnaire.weight = item.answer?.toInt()
                Question.EYE_COLOR -> questionnaire.eye_color = EyeColorType().getServerName(context, item.answer)
                Question.HAIR_LENGTH -> questionnaire.hair_length = HairLengthType().getServerName(context, item.answer)
                Question.HAIR_COLOR -> questionnaire.hair_color = HairColorType().getServerName(context, item.answer)
            }
        }

        //Search
        for (item in searchPageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.PURPOSE_OF_DATING -> questionnaire.purpose = PurposeOfDating().getServerName(context, item.answer)
                Question.WHAT_DO_YOU_WANT -> questionnaire.expectations = ExpectationType().getServerName(context, item.answer)
                Question.SEARCH_LOCATION -> questionnaire.setLocation(item.answer)
                Question.AGE -> questionnaire.setAgeRange(item.answer)
            }
        }

        //Free time
        for (item in freeTimePageQuestionsList.value ?: mutableListOf()) {
            when (item.questionInfo) {
                Question.HOBBY -> questionnaire.hobby = HobbyType().getServerList(context, item.answer)
                Question.TYPES_OF_SPORTS -> questionnaire.sport = SportTypes().getServerList(context, item.answer)
                Question.EVENING_TYPE -> questionnaire.evening_time = EveningTimeType().getServerName(context, item.answer)
            }
        }

        //About me
        questionnaire.about_me = aboutMe

        //Data
        questionnaire.socials = dataPageQuestionsList.value?.firstOrNull {
            it.questionInfo == Question.SOCIAL_NETWORK
        }?.answer?.toListOrNull()

        return questionnaire
    }
}