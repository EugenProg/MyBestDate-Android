package com.bestDate.presentation.questionnarie

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.databinding.ItemConfirmationBinding
import com.bestDate.databinding.ItemMultilineQuestionInfoBinding
import com.bestDate.databinding.ItemRangeBarQuestionnaireBinding
import com.bestDate.presentation.registration.RegistrationHolder
import com.bestDate.presentation.registration.RegistrationType
import com.bestDate.view.questionnaire.list.ConfirmationViewHolder
import com.bestDate.view.questionnaire.list.InfoViewHolder
import com.bestDate.view.questionnaire.list.QuestionnaireQuestion
import com.bestDate.view.questionnaire.list.RangeBarViewHolder

class QuestionnaireItemsList {
    fun getPages(): MutableList<QuestionnairePage> {
        val pageList: MutableList<QuestionnairePage> = ArrayList()

        for (pageNumber in 1..6) {
            pageList.add(getPageByNumber(pageNumber))
        }

        return pageList
    }

    private fun getPageByNumber(number: Int): QuestionnairePage {
        return when (number) {
            1 -> getPersonalPage()
            2 -> getAppearancePage()
            3 -> getSearchPage()
            4 -> getFreeTimePage()
            5 -> getAboutMePage()
            6 -> getDataPage()
            else -> getPersonalPage()
        }
    }

    private fun getPersonalPage(): QuestionnairePage {
        return QuestionnairePage(
            number = 1,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.personal_information,
            questions = QuestionsItemsList().getQuestionsByPageNumber(1)
        )
    }

    private fun getAppearancePage(): QuestionnairePage {
        return QuestionnairePage(
            number = 2,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.your_appearance,
            questions = QuestionsItemsList().getQuestionsByPageNumber(2)
        )
    }

    private fun getSearchPage(): QuestionnairePage {
        return QuestionnairePage(
            number = 3,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.search_condition,
            questions = QuestionsItemsList().getQuestionsByPageNumber(3)
        )
    }

    private fun getFreeTimePage(): QuestionnairePage {
        return QuestionnairePage(
            number = 4,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.your_free_time,
            questions = QuestionsItemsList().getQuestionsByPageNumber(4)
        )
    }

    private fun getAboutMePage(): QuestionnairePage {
        return QuestionnairePage(
            number = 5,
            type = QuestionnairePageType.MULTILINE_INPUT,
            title = R.string.about_me,
            questions = QuestionsItemsList().getQuestionsByPageNumber(5)
        )
    }

    private fun getDataPage(): QuestionnairePage {
        return QuestionnairePage(
            number = 6,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.data_verification,
            questions = QuestionsItemsList().getQuestionsByPageNumber(6),
            nextButtonText = R.string.well_done
        )
    }
}

class QuestionsItemsList {
    fun getQuestionsByPageNumber(page: Int): MutableList<QuestionnaireQuestion>? {
        return when (page) {
            1 -> getPersonalPageQuestions()
            2 -> getAppearancePageQuestions()
            3 -> getSearchPageQuestions()
            4 -> getFreeTimePageQuestions()
            5 -> getAboutMePageQuestions()
            6 -> getDataPageQuestions()
            else -> getPersonalPageQuestions()
        }
    }

    private fun getPersonalPageQuestions(): MutableList<QuestionnaireQuestion> {
        val questions: MutableList<QuestionnaireQuestion> = ArrayList()

        questions.add(QuestionnaireQuestion(questionInfo = Question.MARITAL_STATUS))
        questions.add(QuestionnaireQuestion(questionInfo = Question.HAVING_KIDS))
        questions.add(QuestionnaireQuestion(questionInfo = Question.PLACE_OF_RESIDENCE))
        questions.add(QuestionnaireQuestion(questionInfo = Question.EDUCATION))
        questions.add(QuestionnaireQuestion(questionInfo = Question.OCCUPATIONAL_STATUS))
        return questions
    }

    private fun getAppearancePageQuestions(): MutableList<QuestionnaireQuestion> {
        val questions: MutableList<QuestionnaireQuestion> = ArrayList()

        questions.add(QuestionnaireQuestion(questionInfo = Question.HEIGHT))
        questions.add(QuestionnaireQuestion(questionInfo = Question.WEIGHT))
        questions.add(QuestionnaireQuestion(questionInfo = Question.EYE_COLOR))
        questions.add(QuestionnaireQuestion(questionInfo = Question.HAIR_LENGTH))
        questions.add(QuestionnaireQuestion(questionInfo = Question.HAIR_COLOR))
        return questions
    }

    private fun getSearchPageQuestions(): MutableList<QuestionnaireQuestion> {
        val questions: MutableList<QuestionnaireQuestion> = ArrayList()

        questions.add(QuestionnaireQuestion(questionInfo = Question.PURPOSE_OF_DATING))
        questions.add(QuestionnaireQuestion(questionInfo = Question.WHAT_DO_YOU_WANT))
        questions.add(QuestionnaireQuestion(questionInfo = Question.SEARCH_LOCATION))
        questions.add(QuestionnaireQuestion(questionInfo = Question.AGE))
        return questions
    }

    private fun getFreeTimePageQuestions(): MutableList<QuestionnaireQuestion> {
        val questions: MutableList<QuestionnaireQuestion> = ArrayList()

        questions.add(QuestionnaireQuestion(questionInfo = Question.HOBBY))
        questions.add(QuestionnaireQuestion(questionInfo = Question.TYPES_OF_SPORTS))
        questions.add(QuestionnaireQuestion(questionInfo = Question.EVENING_TYPE))
        return questions
    }

    private fun getAboutMePageQuestions(): MutableList<QuestionnaireQuestion>? {
        return null
    }

    private fun getDataPageQuestions(): MutableList<QuestionnaireQuestion> {
        val questions: MutableList<QuestionnaireQuestion> = ArrayList()

        questions.add(QuestionnaireQuestion(questionInfo = Question.PHOTO, "Your photo is confirmed"))
        questions.add(QuestionnaireQuestion(questionInfo = Question.EMAIL,
            if (RegistrationHolder.type == RegistrationType.EMAIL) RegistrationHolder.login else null))
        questions.add(QuestionnaireQuestion(questionInfo = Question.SOCIAL_NETWORK))
        questions.add(QuestionnaireQuestion(questionInfo = Question.PHONE_NUMBER,
            if (RegistrationHolder.type == RegistrationType.PHONE) RegistrationHolder.login else null))
        return questions
    }
}

enum class Question(
    val question: Int,
    val percent: Int,
    val viewType: QuestionnaireViewType,
    val unitString: Int? = null,
    vararg val answers: Int) {
    MARITAL_STATUS(
        question = R.string.marital_status,
        percent = 5,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.married, R.string.divorced, R.string.single, R.string.it_s_complicated, R.string.in_love, R.string.engaged, R.string.actively_searching),
    HAVING_KIDS(
        question = R.string.having_kids,
        percent = 7,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.no, R.string.one, R.string.two, R.string.three, R.string.four, R.string.five_or_more),
    PLACE_OF_RESIDENCE(
        question =  R.string.plase_of_residence,
        percent = 4,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.AB, R.string.AU, R.string.AT, R.string.AZ, R.string.AL, R.string.DZ, R.string.AS,
        R.string.AI, R.string.AO, R.string.AD, R.string.AR, R.string.AG, R.string.AM, R.string.AW,
        R.string.AF, R.string.BS, R.string.BD, R.string.BB, R.string.BH, R.string.BY, R.string.BZ,
        R.string.BE, R.string.BJ, R.string.BM, R.string.BG, R.string.BO, R.string.BQ, R.string.BA,
        R.string.BW, R.string.BR, R.string.IO, R.string.BN, R.string.BF, R.string.BI, R.string.BT,
        R.string.VU, R.string.HU, R.string.VE, R.string.VG, R.string.VI, R.string.VN, R.string.GA,
        R.string.HT, R.string.GY, R.string.GM, R.string.GH, R.string.GP, R.string.GT, R.string.GN,
        R.string.GW, R.string.DE, R.string.GG, R.string.GI, R.string.HN, R.string.HK, R.string.GD,
        R.string.GL, R.string.GR, R.string.GE, R.string.GU, R.string.DK, R.string.JE, R.string.DJ,
        R.string.DM, R.string.DO, R.string.EG, R.string.ZM, R.string.EH, R.string.ZW, R.string.IL,
        R.string.IN, R.string.ID, R.string.JO, R.string.IQ, R.string.IE, R.string.IS, R.string.ES,
        R.string.IT, R.string.YE, R.string.CV, R.string.KZ, R.string.KH, R.string.CM, R.string.CA,
        R.string.QA, R.string.KE, R.string.CY, R.string.KG, R.string.KI, R.string.CN, R.string.CC,
        R.string.CO, R.string.KM, R.string.CG, R.string.CD, R.string.KP, R.string.KR, R.string.CR,
        R.string.CI, R.string.CU, R.string.KW, R.string.CW, R.string.LA, R.string.LV, R.string.LS,
        R.string.LB, R.string.LY, R.string.LR, R.string.LI, R.string.LT, R.string.LU, R.string.MU,
        R.string.MR, R.string.MG, R.string.YT, R.string.MO, R.string.MW, R.string.MY, R.string.ML,
        R.string.UM, R.string.MV, R.string.MT, R.string.MA, R.string.MQ, R.string.MH, R.string.MX,
        R.string.FM, R.string.MZ, R.string.MD, R.string.MC, R.string.MN, R.string.MS, R.string.MM,
        R.string.NA, R.string.NR, R.string.NP, R.string.NE, R.string.NG, R.string.NL, R.string.NI,
        R.string.NU, R.string.NZ, R.string.NC, R.string.NO, R.string.AE, R.string.OM, R.string.BV,
        R.string.IM, R.string.NF, R.string.CX, R.string.HM, R.string.KY, R.string.CK, R.string.TC,
        R.string.PK, R.string.PW, R.string.PA, R.string.VA, R.string.PG, R.string.PY, R.string.PE,
        R.string.PN, R.string.PL, R.string.PT, R.string.PR, R.string.MK, R.string.RE, R.string.RU,
        R.string.RW, R.string.RO, R.string.WS, R.string.SM, R.string.ST, R.string.SA, R.string.SH,
        R.string.MP, R.string.BL, R.string.MF, R.string.SN, R.string.VC, R.string.KN, R.string.LC,
        R.string.PM, R.string.RS, R.string.SC, R.string.SG, R.string.SX, R.string.SY, R.string.SK,
        R.string.SI, R.string.GB, R.string.US, R.string.SB, R.string.SO, R.string.SD, R.string.SR,
        R.string.SL, R.string.TJ, R.string.TH, R.string.TW, R.string.TZ, R.string.TL, R.string.TG,
        R.string.TK, R.string.TO, R.string.TT, R.string.TV, R.string.TN, R.string.TM, R.string.TR,
        R.string.UG, R.string.UZ, R.string.UA, R.string.WF, R.string.WF, R.string.UY, R.string.FO,
        R.string.FJ, R.string.PH, R.string.FI, R.string.FK, R.string.FR, R.string.GF, R.string.PF,
        R.string.TF, R.string.HR, R.string.CF, R.string.TD, R.string.ME, R.string.CZ, R.string.CL,
        R.string.CH, R.string.SE, R.string.SJ, R.string.LK, R.string.EC, R.string.GQ, R.string.AX,
        R.string.SV, R.string.ER, R.string.SZ, R.string.EE, R.string.ET, R.string.ZA, R.string.GS,
        R.string.OS, R.string.SS, R.string.JM, R.string.JP),
    EDUCATION(
        question = R.string.education,
        percent = 6,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.no_education,R.string.secondary, R.string.higher),
    OCCUPATIONAL_STATUS(
        question = R.string.occupational_status,
        percent = 5,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.student, R.string.working, R.string.unemployed, R.string.businessman, R.string.looking_for_my_self, R.string.freelancer),

    HEIGHT(
        question = R.string.height,
        percent = 6,
        viewType = QuestionnaireViewType.SEEKBAR_VIEW,
        unitString = R.string.cm_unit, 1, 3, 2, 3),
    WEIGHT(
        question = R.string.weight,
        percent = 5,
        viewType = QuestionnaireViewType.SEEKBAR_VIEW,
        unitString = R.string.kg_unit, 30, 150),
    EYE_COLOR(
        question = R.string.eye_color,
        percent = 4,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.blue, R.string.gray, R.string.green, R.string.brown_yellow, R.string.yellow, R.string.brown, R.string.black),
    HAIR_LENGTH(
        question = R.string.hair_length,
        percent = 3,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.short_, R.string.medium, R.string.long_, R.string.no_hair),
    HAIR_COLOR(
        question = R.string.hair_color,
        percent = 5,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.blond, R.string.brown, R.string.brunette, R.string.redhead, R.string.no_hair),

    PURPOSE_OF_DATING(
        question = R.string.purpose_of_dating,
        percent = 8,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.friendship, R.string.love, R.string.communication, R.string.sex),
    WHAT_DO_YOU_WANT(
        question = R.string.what_do_you_want_for_a_date,
        percent = 7,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.having_a_fun, R.string.interesting_communication, R.string.serious_relationship_only, R.string.one_time_sex),
    SEARCH_LOCATION(
        question = R.string.search_location,
        percent = 6,
        viewType = QuestionnaireViewType.INPUT_LOCATION_VIEW,
        unitString = null),
    AGE(
        question = R.string.search_age_range,
        percent = 0,
        viewType = QuestionnaireViewType.RANGE_BAR_VIEW,
        unitString = null, 18, 90),

    /** */
    HOBBY(
        question = R.string.hobby,
        percent = 7,
        viewType = QuestionnaireViewType.MULTILINE_INFO_VIEW,
        unitString = null,
        R.string.music, R.string.dancing, R.string.stand_up),
    TYPES_OF_SPORTS(
        question = R.string.types_of_sports,
        percent = 8,
        viewType = QuestionnaireViewType.MULTILINE_INFO_VIEW,
        unitString = null,
        R.string.badminton, R.string.basketball, R.string.baseball, R.string.billiards, R.string.boxing,
        R.string.wrestling, R.string.bowling, R.string.cycling, R.string.volleyball, R.string.gymnastics,
        R.string.golf, R.string.rowing, R.string.darts, R.string.skating),
    EVENING_TYPE(
        question = R.string.evening_time,
        percent = 7,
        viewType = QuestionnaireViewType.ONE_LINE_INFO_VIEW,
        unitString = null,
        R.string.walking_around_the_city),
    /** */

    PHOTO(
        question = R.string.photo,
        percent = 0,
        viewType = QuestionnaireViewType.CONFIRMATION_VIEW,
        unitString = null),
    EMAIL(
        question = R.string.email,
        percent = 0,
        viewType = QuestionnaireViewType.CONFIRMATION_VIEW,
        unitString = null,
        R.string.your_email_has_not_been_confirmed),
    SOCIAL_NETWORK(
        question = R.string.social_network,
        percent = 0,
        viewType = QuestionnaireViewType.CONFIRMATION_VIEW,
        unitString = null,
        R.string.the_questionnaire_has_not_been_confirmed),
    PHONE_NUMBER(
        question = R.string.phone_number,
        percent = 0,
        viewType = QuestionnaireViewType.CONFIRMATION_VIEW,
        unitString = null,
        R.string.your_phone_number_has_not_been_confirmed)
}

class QuestionnaireViewHolderFactory {
    fun getViewHolder(viewTypeId: Int, parent: ViewGroup): QuestionnaireBaseViewHolder<*> {
        return when(viewTypeId) {
            QuestionnaireViewType.ONE_LINE_INFO_VIEW.id,
            QuestionnaireViewType.MULTILINE_INFO_VIEW.id,
            QuestionnaireViewType.INPUT_LOCATION_VIEW.id,
            QuestionnaireViewType.SEEKBAR_VIEW.id -> InfoViewHolder(
                ItemMultilineQuestionInfoBinding.inflate(
                    createLayoutInflater(parent), parent, false)
            )
            QuestionnaireViewType.CONFIRMATION_VIEW.id -> ConfirmationViewHolder(
                ItemConfirmationBinding.inflate(
                    createLayoutInflater(parent), parent, false)
            )
            QuestionnaireViewType.RANGE_BAR_VIEW.id -> RangeBarViewHolder(
                ItemRangeBarQuestionnaireBinding.inflate(
                    createLayoutInflater(parent), parent, false
                )
            )
            else -> InfoViewHolder(ItemMultilineQuestionInfoBinding.inflate(
                createLayoutInflater(parent), parent, false))
        }
    }

    private fun createLayoutInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }
}