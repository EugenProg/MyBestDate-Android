package com.bestDate.presentation.base.questionnaire

import android.content.Context
import com.bestDate.R
import com.bestDate.data.extension.toList

interface QuestionnaireType {
    fun getName(context: Context, name: String?): String
    fun getServerName(context: Context, answer: String?): String?
}

interface QuestionnaireListType {
    fun getNameLine(context: Context, names: MutableList<String>?): String
    fun getServerList(context: Context, answerLine: String?): MutableList<String>?
}

class MaritalStatus : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Married" -> context.getString(R.string.married)
            "Divorced" -> context.getString(R.string.divorced)
            "Single" -> context.getString(R.string.single)
            "it's complicated" -> context.getString(R.string.it_s_complicated)
            "In love" -> context.getString(R.string.in_love)
            "Engaged" -> context.getString(R.string.engaged)
            "Actively searching" -> context.getString(R.string.actively_searching)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.married) -> "Married"
            context.getString(R.string.divorced) -> "Divorced"
            context.getString(R.string.single) -> "Single"
            context.getString(R.string.it_s_complicated) -> "it's complicated"
            context.getString(R.string.in_love) -> "In love"
            context.getString(R.string.engaged) -> "Engaged"
            context.getString(R.string.actively_searching) -> "Actively searching"
            else -> null
        }
    }
}

class KidsCount : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "No" -> context.getString(R.string.no)
            "1" -> context.getString(R.string.one)
            "2" -> context.getString(R.string.two)
            "3" -> context.getString(R.string.three)
            "4" -> context.getString(R.string.four)
            "5 or more" -> context.getString(R.string.five_or_more)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.one) -> "1"
            context.getString(R.string.two) -> "2"
            context.getString(R.string.three) -> "3"
            context.getString(R.string.four) -> "4"
            context.getString(R.string.five_or_more) -> "5 or more"
            else -> null
        }
    }
}

class EducationStatus : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "No education" -> context.getString(R.string.no_education)
            "Secondary" -> context.getString(R.string.secondary)
            "Higher" -> context.getString(R.string.higher)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.no_education) -> "No education"
            context.getString(R.string.secondary) -> "Secondary"
            context.getString(R.string.higher) -> "Higher"
            else -> null
        }
    }
}

class OccupationalStatus : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Student" -> context.getString(R.string.student)
            "Working" -> context.getString(R.string.working)
            "Unemployed" -> context.getString(R.string.unemployed)
            "Businessman" -> context.getString(R.string.businessman)
            "Looking for myself" -> context.getString(R.string.looking_for_my_self)
            "Freelancer" -> context.getString(R.string.freelancer)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.student) -> "Student"
            context.getString(R.string.working) -> "Working"
            context.getString(R.string.unemployed) -> "Unemployed"
            context.getString(R.string.businessman) -> "Businessman"
            context.getString(R.string.looking_for_my_self) -> "Looking for myself"
            context.getString(R.string.freelancer) -> "Freelancer"
            else -> null
        }
    }
}

class HairColorType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Blond" -> context.getString(R.string.blond)
            "Brunette" -> context.getString(R.string.brunette)
            "Redhead" -> context.getString(R.string.redhead)
            "No hair" -> context.getString(R.string.no_hair)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.blond) -> "Blond"
            context.getString(R.string.brunette) -> "Brunette"
            context.getString(R.string.redhead) -> "Redhead"
            context.getString(R.string.no_hair) -> "No hair"
            else -> null
        }
    }
}

class HairLengthType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Short" -> context.getString(R.string.short_)
            "Medium" -> context.getString(R.string.medium)
            "Long" -> context.getString(R.string.long_)
            "No hair" -> context.getString(R.string.no_hair)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.short_) -> "Short"
            context.getString(R.string.medium) -> "Medium"
            context.getString(R.string.long_) -> "Long"
            context.getString(R.string.no_hair) -> "No hair"
            else -> null
        }
    }
}

class EyeColorType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Blue" -> context.getString(R.string.blue)
            "Gray" -> context.getString(R.string.gray)
            "Green" -> context.getString(R.string.green)
            "Brown-yellow" -> context.getString(R.string.brown_yellow)
            "Yellow" -> context.getString(R.string.yellow)
            "Brown" -> context.getString(R.string.brown)
            "Black" -> context.getString(R.string.black)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.blue) -> "Blue"
            context.getString(R.string.gray) -> "Gray"
            context.getString(R.string.green) -> "Green"
            context.getString(R.string.brown_yellow) -> "Brown-yellow"
            context.getString(R.string.yellow) -> "Yellow"
            context.getString(R.string.brown) -> "Brown"
            context.getString(R.string.black) -> "Black"
            else -> null
        }
    }
}

class PurposeOfDating : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Friendship" -> context.getString(R.string.friendship)
            "Love" -> context.getString(R.string.love)
            "Communication" -> context.getString(R.string.communication)
            "Sex" -> context.getString(R.string.sex)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.friendship) -> "Friendship"
            context.getString(R.string.love) -> "Love"
            context.getString(R.string.communication) -> "Communication"
            context.getString(R.string.sex) -> "Sex"
            else -> null
        }
    }
}

class ExpectationType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Having a fun" -> context.getString(R.string.having_a_fun)
            "Interesting communication" -> context.getString(R.string.interesting_communication)
            "Serious relationship only" -> context.getString(R.string.serious_relationship_only)
            "Serious relationship" -> context.getString(R.string.serious_relationship)
            "One time sex" -> context.getString(R.string.one_time_sex)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.having_a_fun) -> "Having a fun"
            context.getString(R.string.interesting_communication) -> "Interesting communication"
            context.getString(R.string.serious_relationship_only) -> "Serious relationship only"
            context.getString(R.string.serious_relationship) -> "Serious relationship"
            context.getString(R.string.one_time_sex) -> "One time sex"
            else -> null
        }
    }
}

class EveningTimeType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Walking around the city" -> context.getString(R.string.walking_around_the_city)
            "Sitting in Bestdate" -> context.getString(R.string.sitting_in_best_date)
            "Going on dates" -> context.getString(R.string.going_on_dates)
            "Netflix" -> context.getString(R.string.netflix)
            "Youtube" -> context.getString(R.string.youtube)
            "Meditating" -> context.getString(R.string.meditating)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.walking_around_the_city) -> "Walking around the city"
            context.getString(R.string.sitting_in_best_date) -> "Sitting in Bestdate"
            context.getString(R.string.going_on_dates) -> "Going on dates"
            context.getString(R.string.netflix) -> "Netflix"
            context.getString(R.string.youtube) -> "Youtube"
            context.getString(R.string.meditating) -> "Meditating"
            else -> null
        }
    }
}

class NationalityType : QuestionnaireType {
    override fun getName(context: Context, name: String?): String {
        return when (name) {
            "Abkhazia" -> context.getString(R.string.AB)
            "Australia" -> context.getString(R.string.AU)
            "Austria" -> context.getString(R.string.AT)
            "Azerbaijan" -> context.getString(R.string.AZ)
            "Albania" -> context.getString(R.string.AL)
            "Algeria" -> context.getString(R.string.DZ)
            "American Samoa" -> context.getString(R.string.AS)
            "Anguilla" -> context.getString(R.string.AI)
            "Angola" -> context.getString(R.string.AO)
            "Andorra" -> context.getString(R.string.AD)
            "Antigua and Barbuda" -> context.getString(R.string.AR)
            "Argentina" -> context.getString(R.string.AG)
            "Armenia" -> context.getString(R.string.AM)
            "Aruba" -> context.getString(R.string.AW)
            "Afghanistan" -> context.getString(R.string.AF)
            "Bahamas" -> context.getString(R.string.BS)
            "Bangladesh" -> context.getString(R.string.BD)
            "Barbados" -> context.getString(R.string.BB)
            "Bahrain" -> context.getString(R.string.BH)
            "Belarus" -> context.getString(R.string.BY)
            "Belize" -> context.getString(R.string.BZ)
            "Belgium" -> context.getString(R.string.BE)
            "Benin" -> context.getString(R.string.BJ)
            "Bermuda" -> context.getString(R.string.BM)
            "Bulgaria" -> context.getString(R.string.BG)
            "Bolivia" -> context.getString(R.string.BO)
            "Bonaire" -> context.getString(R.string.BQ)
            "Bosnia and Herzegovina" -> context.getString(R.string.BA)
            "Botswana" -> context.getString(R.string.BW)
            "Brazil" -> context.getString(R.string.BR)
            "British Indian Ocean Territory" -> context.getString(R.string.IO)
            "Brunei Darussalam" -> context.getString(R.string.BN)
            "Burkina Faso" -> context.getString(R.string.BF)
            "Burundi" -> context.getString(R.string.BI)
            "Bhutan" -> context.getString(R.string.BT)
            "Vanuatu" -> context.getString(R.string.VU)
            "Hungary" -> context.getString(R.string.HU)
            "Venezuela" -> context.getString(R.string.VE)
            "Virgin Islands, British" -> context.getString(R.string.VG)
            "Virgin Islands, U.S." -> context.getString(R.string.VI)
            "Vietnam" -> context.getString(R.string.VN)
            "Gabon" -> context.getString(R.string.GA)
            "Haiti" -> context.getString(R.string.HT)
            "Guyana" -> context.getString(R.string.GY)
            "Gambia" -> context.getString(R.string.GM)
            "Ghana" -> context.getString(R.string.GH)
            "Guadeloupe" -> context.getString(R.string.GP)
            "Guatemala" -> context.getString(R.string.GT)
            "Guinea" -> context.getString(R.string.GN)
            "Guinea-Bissau" -> context.getString(R.string.GW)
            "Germany" -> context.getString(R.string.DE)
            "Guernsey" -> context.getString(R.string.GG)
            "Gibraltar" -> context.getString(R.string.GI)
            "Honduras" -> context.getString(R.string.HN)
            "Hong Kong" -> context.getString(R.string.HK)
            "Grenada" -> context.getString(R.string.GD)
            "Greenland" -> context.getString(R.string.GL)
            "Greece" -> context.getString(R.string.GR)
            "Georgia" -> context.getString(R.string.GE)
            "Guam" -> context.getString(R.string.GU)
            "Denmark" -> context.getString(R.string.DK)
            "Jerse" -> context.getString(R.string.JE)
            "Djibouti" -> context.getString(R.string.DJ)
            "Dominica" -> context.getString(R.string.DM)
            "Dominican Republic" -> context.getString(R.string.DO)
            "Egypt" -> context.getString(R.string.EG)
            "Zambia" -> context.getString(R.string.ZM)
            "Western Sahara" -> context.getString(R.string.EH)
            "Zimbabwe" -> context.getString(R.string.ZW)
            "Israel" -> context.getString(R.string.IL)
            "India" -> context.getString(R.string.IN)
            "Indonesia" -> context.getString(R.string.ID)
            "Jordan" -> context.getString(R.string.JO)
            "Iraq" -> context.getString(R.string.IQ)
            "Ireland" -> context.getString(R.string.IE)
            "Iceland" -> context.getString(R.string.IS)
            "Spain" -> context.getString(R.string.ES)
            "Italy" -> context.getString(R.string.IT)
            "Yemen" -> context.getString(R.string.YE)
            "Cape Verde" -> context.getString(R.string.CV)
            "Kazakhstan" -> context.getString(R.string.KZ)
            "Cambodia" -> context.getString(R.string.KH)
            "Cameroon" -> context.getString(R.string.CM)
            "Canada" -> context.getString(R.string.CA)
            "Qatar" -> context.getString(R.string.QA)
            "Kenya" -> context.getString(R.string.KE)
            "Cyprus" -> context.getString(R.string.CY)
            "Kyrgyzstan" -> context.getString(R.string.KG)
            "Kiribati" -> context.getString(R.string.KI)
            "China" -> context.getString(R.string.CN)
            "Cocos (Keeling) Islands" -> context.getString(R.string.CC)
            "Colombia" -> context.getString(R.string.CO)
            "Comoros" -> context.getString(R.string.KM)
            "Congo" -> context.getString(R.string.CG)
            "Congo, Democratic Republic of the" -> context.getString(R.string.CD)
            "Korea, Democratic People's republic of" -> context.getString(R.string.KP)
            "Korea, Republic of" -> context.getString(R.string.KR)
            "Costa Rica" -> context.getString(R.string.CR)
            "Cote d'Ivoire" -> context.getString(R.string.CI)
            "Cuba" -> context.getString(R.string.CU)
            "Kuwait" -> context.getString(R.string.KW)
            "Curaçao" -> context.getString(R.string.CW)
            "Lao People's Democratic Republic" -> context.getString(R.string.LA)
            "Latvia" -> context.getString(R.string.LV)
            "Lesotho" -> context.getString(R.string.LS)
            "Lebanon" -> context.getString(R.string.LB)
            "Libyan Arab Jamahiriya" -> context.getString(R.string.LY)
            "Liberia" -> context.getString(R.string.LR)
            "Liechtenstein" -> context.getString(R.string.LI)
            "Lithuania" -> context.getString(R.string.LT)
            "Luxembourg" -> context.getString(R.string.LU)
            "Mauritius" -> context.getString(R.string.MU)
            "Mauritania" -> context.getString(R.string.MR)
            "Madagascar" -> context.getString(R.string.MG)
            "Mayotte" -> context.getString(R.string.YT)
            "Macao" -> context.getString(R.string.MO)
            "Malawi" -> context.getString(R.string.MW)
            "Malaysia" -> context.getString(R.string.MY)
            "Mali" -> context.getString(R.string.ML)
            "United States Minor Outlying Islands" -> context.getString(R.string.UM)
            "Maldives" -> context.getString(R.string.MV)
            "Malta" -> context.getString(R.string.MT)
            "Morocco" -> context.getString(R.string.MA)
            "Martinique" -> context.getString(R.string.MQ)
            "Marshall Islands" -> context.getString(R.string.MH)
            "Mexico" -> context.getString(R.string.MX)
            "Micronesia, Federated States of" -> context.getString(R.string.FM)
            "Mozambique" -> context.getString(R.string.MZ)
            "Moldova" -> context.getString(R.string.MD)
            "Monaco" -> context.getString(R.string.MC)
            "Mongolia" -> context.getString(R.string.MN)
            "Montserrat" -> context.getString(R.string.MS)
            "Burma" -> context.getString(R.string.MM)
            "Namibia" -> context.getString(R.string.NA)
            "Nauru" -> context.getString(R.string.NR)
            "Nepal" -> context.getString(R.string.NP)
            "Niger" -> context.getString(R.string.NE)
            "Nigeria" -> context.getString(R.string.NG)
            "Netherlands" -> context.getString(R.string.NL)
            "Nicaragua" -> context.getString(R.string.NI)
            "Niu" -> context.getString(R.string.NU)
            "New Zealand" -> context.getString(R.string.NZ)
            "New Caledonia" -> context.getString(R.string.NC)
            "Norway" -> context.getString(R.string.NO)
            "United Arab Emirates" -> context.getString(R.string.AE)
            "Oman" -> context.getString(R.string.OM)
            "Bouvet Island" -> context.getString(R.string.BV)
            "Isle of Man" -> context.getString(R.string.IM)
            "Norfolk Island" -> context.getString(R.string.NF)
            "Christmas Island" -> context.getString(R.string.CX)
            "Heard Island and McDonald Islands" -> context.getString(R.string.HM)
            "Cayman Islands" -> context.getString(R.string.KY)
            "Cook Islands" -> context.getString(R.string.CK)
            "Turks and Caicos Islands" -> context.getString(R.string.TC)
            "Pakistan" -> context.getString(R.string.PK)
            "Palau" -> context.getString(R.string.PW)
            "Panama" -> context.getString(R.string.PA)
            "Holy See (Vatican City State)" -> context.getString(R.string.VA)
            "Papua New Guinea" -> context.getString(R.string.PG)
            "Paraguay" -> context.getString(R.string.PY)
            "Peru" -> context.getString(R.string.PE)
            "Pitcairn" -> context.getString(R.string.PN)
            "Poland" -> context.getString(R.string.PL)
            "Portugal" -> context.getString(R.string.PT)
            "Puerto Rico" -> context.getString(R.string.PR)
            "Macedonia, The Former Yugoslav Republic Of" -> context.getString(R.string.MK)
            "Reunion" -> context.getString(R.string.RE)
            "Russian Federation" -> context.getString(R.string.RU)
            "Rwanda" -> context.getString(R.string.RW)
            "Romania" -> context.getString(R.string.RO)
            "Samoa" -> context.getString(R.string.WS)
            "San Marino" -> context.getString(R.string.SM)
            "Sao Tome and Principe" -> context.getString(R.string.ST)
            "Saudi Arabia" -> context.getString(R.string.SA)
            "Saint Helena, Ascension And Tristan Da Cunha" -> context.getString(R.string.SH)
            "Northern Mariana Islands" -> context.getString(R.string.MP)
            "Saint Barthélemy" -> context.getString(R.string.BL)
            "Saint Martin (French Part)" -> context.getString(R.string.MF)
            "Senegal" -> context.getString(R.string.SN)
            "Saint Vincent and the Grenadines" -> context.getString(R.string.VC)
            "Saint Kitts and Nevis" -> context.getString(R.string.KN)
            "Saint Lucia" -> context.getString(R.string.LC)
            "Saint Pierre and Miquelon" -> context.getString(R.string.PM)
            "Serbia" -> context.getString(R.string.RS)
            "Seychelles" -> context.getString(R.string.SC)
            "Singapore" -> context.getString(R.string.SG)
            "Sint Maarten" -> context.getString(R.string.SX)
            "Syrian Arab Republic" -> context.getString(R.string.SY)
            "Slovakia" -> context.getString(R.string.SK)
            "Slovenia" -> context.getString(R.string.SI)
            "United Kingdom" -> context.getString(R.string.GB)
            "United States" -> context.getString(R.string.US)
            "Solomon Islands" -> context.getString(R.string.SB)
            "Somalia" -> context.getString(R.string.SO)
            "Sudan" -> context.getString(R.string.SD)
            "Suriname" -> context.getString(R.string.SR)
            "Sierra Leone" -> context.getString(R.string.SL)
            "Tajikistan" -> context.getString(R.string.TJ)
            "Thailand" -> context.getString(R.string.TH)
            "Taiwan" -> context.getString(R.string.TW)
            "Tanzania, United Republic Of" -> context.getString(R.string.TZ)
            "Timor-Leste" -> context.getString(R.string.TL)
            "Togo" -> context.getString(R.string.TG)
            "Tokelau" -> context.getString(R.string.TK)
            "Tonga" -> context.getString(R.string.TO)
            "Trinidad and Tobago" -> context.getString(R.string.TT)
            "Tuvalu" -> context.getString(R.string.TV)
            "Tunisia" -> context.getString(R.string.TN)
            "Turkmenistan" -> context.getString(R.string.TM)
            "Turkey" -> context.getString(R.string.TR)
            "Uganda" -> context.getString(R.string.UG)
            "Uzbekistan" -> context.getString(R.string.UZ)
            "Ukraine" -> context.getString(R.string.UA)
            "Wallis and Futuna" -> context.getString(R.string.WF)
            "Uruguay" -> context.getString(R.string.UY)
            "Faroe Islands" -> context.getString(R.string.FO)
            "Fiji" -> context.getString(R.string.FJ)
            "Philippines" -> context.getString(R.string.PH)
            "Finland" -> context.getString(R.string.FI)
            "Falkland Islands (Malvinas)" -> context.getString(R.string.FK)
            "France" -> context.getString(R.string.FR)
            "French Guiana" -> context.getString(R.string.GF)
            "French Polynesia" -> context.getString(R.string.PF)
            "French Southern Territories" -> context.getString(R.string.TF)
            "Croatia" -> context.getString(R.string.HR)
            "Central African Republic" -> context.getString(R.string.CF)
            "Chad" -> context.getString(R.string.TD)
            "Montenegro" -> context.getString(R.string.ME)
            "Czech Republic" -> context.getString(R.string.CZ)
            "Chile" -> context.getString(R.string.CL)
            "Switzerland" -> context.getString(R.string.CH)
            "Sweden" -> context.getString(R.string.SE)
            "Svalbard and Jan Mayen" -> context.getString(R.string.SJ)
            "Sri Lanka" -> context.getString(R.string.LK)
            "Ecuador" -> context.getString(R.string.EC)
            "Equatorial Guinea" -> context.getString(R.string.GQ)
            "Åland Islands" -> context.getString(R.string.AX)
            "El Salvador" -> context.getString(R.string.SV)
            "Eritrea" -> context.getString(R.string.ER)
            "Eswatini" -> context.getString(R.string.SZ)
            "Estonia" -> context.getString(R.string.EE)
            "Ethiopia" -> context.getString(R.string.ET)
            "South Africa" -> context.getString(R.string.ZA)
            "South Georgia and the South Sandwich Islands" -> context.getString(R.string.GS)
            "South Ossetia" -> context.getString(R.string.OS)
            "South Sudan" -> context.getString(R.string.SS)
            "Jamaica" -> context.getString(R.string.JM)
            "Japan" -> context.getString(R.string.JP)
            else -> ""
        }
    }

    override fun getServerName(context: Context, answer: String?): String? {
        return when (answer) {
            context.getString(R.string.AB) -> "Abkhazia"
            context.getString(R.string.AU) -> "Australia"
            context.getString(R.string.AT) -> "Austria"
            context.getString(R.string.AZ) -> "Azerbaijan"
            context.getString(R.string.AL) -> "Albania"
            context.getString(R.string.DZ) -> "Algeria"
            context.getString(R.string.AS) -> "American Samoa"
            context.getString(R.string.AI) -> "Anguilla"
            context.getString(R.string.AO) -> "Angola"
            context.getString(R.string.AD) -> "Andorra"
            context.getString(R.string.AR) -> "Antigua and Barbuda"
            context.getString(R.string.AG) -> "Argentina"
            context.getString(R.string.AM) -> "Armenia"
            context.getString(R.string.AW) -> "Aruba"
            context.getString(R.string.AF) -> "Afghanistan"
            context.getString(R.string.BS) -> "Bahamas"
            context.getString(R.string.BD) -> "Bangladesh"
            context.getString(R.string.BB) -> "Barbados"
            context.getString(R.string.BH) -> "Bahrain"
            context.getString(R.string.BY) -> "Belarus"
            context.getString(R.string.BZ) -> "Belize"
            context.getString(R.string.BE) -> "Belgium"
            context.getString(R.string.BJ) -> "Benin"
            context.getString(R.string.BM) -> "Bermuda"
            context.getString(R.string.BG) -> "Bulgaria"
            context.getString(R.string.BO) -> "Bolivia"
            context.getString(R.string.BQ) -> "Bonaire"
            context.getString(R.string.BA) -> "Bosnia and Herzegovina"
            context.getString(R.string.BW) -> "Botswana"
            context.getString(R.string.BR) -> "Brazil"
            context.getString(R.string.IO) -> "British Indian Ocean Territory"
            context.getString(R.string.BN) -> "Brunei Darussalam"
            context.getString(R.string.BF) -> "Burkina Faso"
            context.getString(R.string.BI) -> "Burundi"
            context.getString(R.string.BT) -> "Bhutan"
            context.getString(R.string.VU) -> "Vanuatu"
            context.getString(R.string.HU) -> "Hungary"
            context.getString(R.string.VE) -> "Venezuela"
            context.getString(R.string.VG) -> "Virgin Islands, British"
            context.getString(R.string.VI) -> "Virgin Islands, U.S."
            context.getString(R.string.VN) -> "Vietnam"
            context.getString(R.string.GA) -> "Gabon"
            context.getString(R.string.HT) -> "Haiti"
            context.getString(R.string.GY) -> "Guyana"
            context.getString(R.string.GM) -> "Gambia"
            context.getString(R.string.GH) -> "Ghana"
            context.getString(R.string.GP) -> "Guadeloupe"
            context.getString(R.string.GT) -> "Guatemala"
            context.getString(R.string.GN) -> "Guinea"
            context.getString(R.string.GW) -> "Guinea-Bissau"
            context.getString(R.string.DE) -> "Germany"
            context.getString(R.string.GG) -> "Guernsey"
            context.getString(R.string.GI) -> "Gibraltar"
            context.getString(R.string.HN) -> "Honduras"
            context.getString(R.string.HK) -> "Hong Kong"
            context.getString(R.string.GD) -> "Grenada"
            context.getString(R.string.GL) -> "Greenland"
            context.getString(R.string.GR) -> "Greece"
            context.getString(R.string.GE) -> "Georgia"
            context.getString(R.string.GU) -> "Guam"
            context.getString(R.string.DK) -> "Denmark"
            context.getString(R.string.JE) -> "Jerse"
            context.getString(R.string.DJ) -> "Djibouti"
            context.getString(R.string.DM) -> "Dominica"
            context.getString(R.string.DO) -> "Dominican Republic"
            context.getString(R.string.EG) -> "Egypt"
            context.getString(R.string.ZM) -> "Zambia"
            context.getString(R.string.EH) -> "Western Sahara"
            context.getString(R.string.ZW) -> "Zimbabwe"
            context.getString(R.string.IL) -> "Israel"
            context.getString(R.string.IN) -> "India"
            context.getString(R.string.ID) -> "Indonesia"
            context.getString(R.string.JO) -> "Jordan"
            context.getString(R.string.IQ) -> "Iraq"
            context.getString(R.string.IE) -> "Ireland"
            context.getString(R.string.IS) -> "Iceland"
            context.getString(R.string.ES) -> "Spain"
            context.getString(R.string.IT) -> "Italy"
            context.getString(R.string.YE) -> "Yemen"
            context.getString(R.string.CV) -> "Cape Verde"
            context.getString(R.string.KZ) -> "Kazakhstan"
            context.getString(R.string.KH) -> "Cambodia"
            context.getString(R.string.CM) -> "Cameroon"
            context.getString(R.string.CA) -> "Canada"
            context.getString(R.string.QA) -> "Qatar"
            context.getString(R.string.KE) -> "Kenya"
            context.getString(R.string.CY) -> "Cyprus"
            context.getString(R.string.KG) -> "Kyrgyzstan"
            context.getString(R.string.KI) -> "Kiribati"
            context.getString(R.string.CN) -> "China"
            context.getString(R.string.CC) -> "Cocos (Keeling) Islands"
            context.getString(R.string.CO) -> "Colombia"
            context.getString(R.string.KM) -> "Comoros"
            context.getString(R.string.CG) -> "Congo"
            context.getString(R.string.CD) -> "Congo, Democratic Republic of the"
            context.getString(R.string.KP) -> "Korea, Democratic People's republic of"
            context.getString(R.string.KR) -> "Korea, Republic of"
            context.getString(R.string.CR) -> "Costa Rica"
            context.getString(R.string.CI) -> "Cote d'Ivoire"
            context.getString(R.string.CU) -> "Cuba"
            context.getString(R.string.KW) -> "Kuwait"
            context.getString(R.string.CW) -> "Curaçao"
            context.getString(R.string.LA) -> "Lao People's Democratic Republic"
            context.getString(R.string.LV) -> "Latvia"
            context.getString(R.string.LS) -> "Lesotho"
            context.getString(R.string.LB) -> "Lebanon"
            context.getString(R.string.LY) -> "Libyan Arab Jamahiriya"
            context.getString(R.string.LR) -> "Liberia"
            context.getString(R.string.LI) -> "Liechtenstein"
            context.getString(R.string.LT) -> "Lithuania"
            context.getString(R.string.LU) -> "Luxembourg"
            context.getString(R.string.MU) -> "Mauritius"
            context.getString(R.string.MR) -> "Mauritania"
            context.getString(R.string.MG) -> "Madagascar"
            context.getString(R.string.YT) -> "Mayotte"
            context.getString(R.string.MO) -> "Macao"
            context.getString(R.string.MW) -> "Malawi"
            context.getString(R.string.MY) -> "Malaysia"
            context.getString(R.string.ML) -> "Mali"
            context.getString(R.string.UM) -> "United States Minor Outlying Islands"
            context.getString(R.string.MV) -> "Maldives"
            context.getString(R.string.MT) -> "Malta"
            context.getString(R.string.MA) -> "Morocco"
            context.getString(R.string.MQ) -> "Martinique"
            context.getString(R.string.MH) -> "Marshall Islands"
            context.getString(R.string.MX) -> "Mexico"
            context.getString(R.string.FM) -> "Micronesia, Federated States of"
            context.getString(R.string.MZ) -> "Mozambique"
            context.getString(R.string.MD) -> "Moldova"
            context.getString(R.string.MC) -> "Monaco"
            context.getString(R.string.MN) -> "Mongolia"
            context.getString(R.string.MS) -> "Montserrat"
            context.getString(R.string.MM) -> "Burma"
            context.getString(R.string.NA) -> "Namibia"
            context.getString(R.string.NR) -> "Nauru"
            context.getString(R.string.NP) -> "Nepal"
            context.getString(R.string.NE) -> "Niger"
            context.getString(R.string.NG) -> "Nigeria"
            context.getString(R.string.NL) -> "Netherlands"
            context.getString(R.string.NI) -> "Nicaragua"
            context.getString(R.string.NU) -> "Niu"
            context.getString(R.string.NZ) -> "New Zealand"
            context.getString(R.string.NC) -> "New Caledonia"
            context.getString(R.string.NO) -> "Norway"
            context.getString(R.string.AE) -> "United Arab Emirates"
            context.getString(R.string.OM) -> "Oman"
            context.getString(R.string.BV) -> "Bouvet Island"
            context.getString(R.string.IM) -> "Isle of Man"
            context.getString(R.string.NF) -> "Norfolk Island"
            context.getString(R.string.CX) -> "Christmas Island"
            context.getString(R.string.HM) -> "Heard Island and McDonald Islands"
            context.getString(R.string.KY) -> "Cayman Islands"
            context.getString(R.string.CK) -> "Cook Islands"
            context.getString(R.string.TC) -> "Turks and Caicos Islands"
            context.getString(R.string.PK) -> "Pakistan"
            context.getString(R.string.PW) -> "Palau"
            context.getString(R.string.PA) -> "Panama"
            context.getString(R.string.VA) -> "Holy See (Vatican City State)"
            context.getString(R.string.PG) -> "Papua New Guinea"
            context.getString(R.string.PY) -> "Paraguay"
            context.getString(R.string.PE) -> "Peru"
            context.getString(R.string.PN) -> "Pitcairn"
            context.getString(R.string.PL) -> "Poland"
            context.getString(R.string.PT) -> "Portugal"
            context.getString(R.string.PR) -> "Puerto Rico"
            context.getString(R.string.MK) -> "Macedonia, The Former Yugoslav Republic Of"
            context.getString(R.string.RE) -> "Reunion"
            context.getString(R.string.RU) -> "Russian Federation"
            context.getString(R.string.RW) -> "Rwanda"
            context.getString(R.string.RO) -> "Romania"
            context.getString(R.string.WS) -> "Samoa"
            context.getString(R.string.SM) -> "San Marino"
            context.getString(R.string.ST) -> "Sao Tome and Principe"
            context.getString(R.string.SA) -> "Saudi Arabia"
            context.getString(R.string.SH) -> "Saint Helena, Ascension And Tristan Da Cunha"
            context.getString(R.string.MP) -> "Northern Mariana Islands"
            context.getString(R.string.BL) -> "Saint Barthélemy"
            context.getString(R.string.MF) -> "Saint Martin (French Part)"
            context.getString(R.string.SN) -> "Senegal"
            context.getString(R.string.VC) -> "Saint Vincent and the Grenadines"
            context.getString(R.string.KN) -> "Saint Kitts and Nevis"
            context.getString(R.string.LC) -> "Saint Lucia"
            context.getString(R.string.PM) -> "Saint Pierre and Miquelon"
            context.getString(R.string.RS) -> "Serbia"
            context.getString(R.string.SC) -> "Seychelles"
            context.getString(R.string.SG) -> "Singapore"
            context.getString(R.string.SX) -> "Sint Maarten"
            context.getString(R.string.SY) -> "Syrian Arab Republic"
            context.getString(R.string.SK) -> "Slovakia"
            context.getString(R.string.SI) -> "Slovenia"
            context.getString(R.string.GB) -> "United Kingdom"
            context.getString(R.string.US) -> "United States"
            context.getString(R.string.SB) -> "Solomon Islands"
            context.getString(R.string.SO) -> "Somalia"
            context.getString(R.string.SD) -> "Sudan"
            context.getString(R.string.SR) -> "Suriname"
            context.getString(R.string.SL) -> "Sierra Leone"
            context.getString(R.string.TJ) -> "Tajikistan"
            context.getString(R.string.TH) -> "Thailand"
            context.getString(R.string.TW) -> "Taiwan"
            context.getString(R.string.TZ) -> "Tanzania, United Republic Of"
            context.getString(R.string.TL) -> "Timor-Leste"
            context.getString(R.string.TG) -> "Togo"
            context.getString(R.string.TK) -> "Tokelau"
            context.getString(R.string.TO) -> "Tonga"
            context.getString(R.string.TT) -> "Trinidad and Tobago"
            context.getString(R.string.TV) -> "Tuvalu"
            context.getString(R.string.TN) -> "Tunisia"
            context.getString(R.string.TM) -> "Turkmenistan"
            context.getString(R.string.TR) -> "Turkey"
            context.getString(R.string.UG) -> "Uganda"
            context.getString(R.string.UZ) -> "Uzbekistan"
            context.getString(R.string.UA) -> "Ukraine"
            context.getString(R.string.WF) -> "Wallis and Futuna"
            context.getString(R.string.UY) -> "Uruguay"
            context.getString(R.string.FO) -> "Faroe Islands"
            context.getString(R.string.FJ) -> "Fiji"
            context.getString(R.string.PH) -> "Philippines"
            context.getString(R.string.FI) -> "Finland"
            context.getString(R.string.FK) -> "Falkland Islands (Malvinas)"
            context.getString(R.string.FR) -> "France"
            context.getString(R.string.GF) -> "French Guiana"
            context.getString(R.string.PF) -> "French Polynesia"
            context.getString(R.string.TF) -> "French Southern Territories"
            context.getString(R.string.HR) -> "Croatia"
            context.getString(R.string.CF) -> "Central African Republic"
            context.getString(R.string.TD) -> "Chad"
            context.getString(R.string.ME) -> "Montenegro"
            context.getString(R.string.CZ) -> "Czech Republic"
            context.getString(R.string.CL) -> "Chile"
            context.getString(R.string.CH) -> "Switzerland"
            context.getString(R.string.SE) -> "Sweden"
            context.getString(R.string.SJ) -> "Svalbard and Jan Mayen"
            context.getString(R.string.LK) -> "Sri Lanka"
            context.getString(R.string.EC) -> "Ecuador"
            context.getString(R.string.GQ) -> "Equatorial Guinea"
            context.getString(R.string.AX) -> "Åland Islands"
            context.getString(R.string.SV) -> "El Salvador"
            context.getString(R.string.ER) -> "Eritrea"
            context.getString(R.string.SZ) -> "Eswatini"
            context.getString(R.string.EE) -> "Estonia"
            context.getString(R.string.ET) -> "Ethiopia"
            context.getString(R.string.ZA) -> "South Africa"
            context.getString(R.string.GS) -> "South Georgia and the South Sandwich Islands"
            context.getString(R.string.OS) -> "South Ossetia"
            context.getString(R.string.SS) -> "South Sudan"
            context.getString(R.string.JM) -> "Jamaica"
            context.getString(R.string.JP) -> "Japan"
            else -> null
        }
    }
}

class HobbyType : QuestionnaireListType {
    override fun getNameLine(context: Context, names: MutableList<String>?): String {
        if (names.isNullOrEmpty()) return ""
        val list: MutableList<String> = mutableListOf()
        names.forEach {
            list.add(
                when (it) {
                    "Music" -> context.getString(R.string.music)
                    "Dancing" -> context.getString(R.string.dancing)
                    "Stand up" -> context.getString(R.string.stand_up)
                    "Fishing" -> context.getString(R.string.fishing)
                    "Diggering" -> context.getString(R.string.diggering)
                    "Hunting" -> context.getString(R.string.hunting)
                    "Blogging" -> context.getString(R.string.blogging)
                    "Hike" -> context.getString(R.string.hike)
                    "Quest" -> context.getString(R.string.quest)
                    "Gardening" -> context.getString(R.string.gardening)
                    "Watching movies" -> context.getString(R.string.watching_movies)
                    "Bike" -> context.getString(R.string.bike)
                    "Nature Watching" -> context.getString(R.string.nature_watching)
                    "Spotting" -> context.getString(R.string.spotting)
                    "Sailing" -> context.getString(R.string.sailing)
                    "Rap" -> context.getString(R.string.rap)
                    "Road trips" -> context.getString(R.string.road_trips)
                    "Pick-up" -> context.getString(R.string.pickup)
                    "Bath or sauna" -> context.getString(R.string.sauna)
                    "Radio amateurism" -> context.getString(R.string.radio)
                    else -> ""
                }
            )
        }

        return list.joinToString()
    }

    override fun getServerList(
        context: Context,
        answerLine: String?
    ): MutableList<String>? {
        val serverList: MutableList<String> = mutableListOf()
        if (answerLine.isNullOrEmpty()) return null

        answerLine.toList()?.forEach {
            serverList.add(
                when (it) {
                    context.getString(R.string.music) -> "Music"
                    context.getString(R.string.dancing) -> "Dancing"
                    context.getString(R.string.stand_up) -> "Stand up"
                    context.getString(R.string.fishing) -> "Fishing"
                    context.getString(R.string.diggering) -> "Diggering"
                    context.getString(R.string.hunting) -> "Hunting"
                    context.getString(R.string.blogging) -> "Blogging"
                    context.getString(R.string.hike) -> "Hike"
                    context.getString(R.string.quest) -> "Quest"
                    context.getString(R.string.gardening) -> "Gardening"
                    context.getString(R.string.watching_movies) -> "Watching movies"
                    context.getString(R.string.bike) -> "Bike"
                    context.getString(R.string.nature_watching) -> "Nature Watching"
                    context.getString(R.string.spotting) -> "Spotting"
                    context.getString(R.string.sailing) -> "Sailing"
                    context.getString(R.string.rap) -> "Rap"
                    context.getString(R.string.road_trips) -> "Road trips"
                    context.getString(R.string.pickup) -> "Pick-up"
                    context.getString(R.string.sauna) -> "Bath or sauna"
                    context.getString(R.string.radio) -> "Radio amateurism"
                    else -> ""
                }
            )
        }

        return serverList
    }
}

class SportTypes : QuestionnaireListType {
    override fun getNameLine(context: Context, names: MutableList<String>?): String {
        if (names.isNullOrEmpty()) return ""
        val list: MutableList<String> = mutableListOf()
        names.forEach {
            list.add(
                when (it) {
                    "Badminton" -> context.getString(R.string.badminton)
                    "Basketball" -> context.getString(R.string.basketball)
                    "Baseball" -> context.getString(R.string.baseball)
                    "Billiards" -> context.getString(R.string.billiards)
                    "Boxing" -> context.getString(R.string.boxing)
                    "Wrestling" -> context.getString(R.string.wrestling)
                    "Bowling" -> context.getString(R.string.bowling)
                    "Cycling" -> context.getString(R.string.cycling)
                    "Volleyball" -> context.getString(R.string.volleyball)
                    "Gymnastics" -> context.getString(R.string.gymnastics)
                    "Golf" -> context.getString(R.string.golf)
                    "Rowing" -> context.getString(R.string.rowing)
                    "Darts" -> context.getString(R.string.darts)
                    "Skating" -> context.getString(R.string.skating)
                    "Karate" -> context.getString(R.string.karate)
                    "Tennis" -> context.getString(R.string.tennis)
                    "Swimming" -> context.getString(R.string.swimming)
                    "Judo" -> context.getString(R.string.judo)
                    "Climbing" -> context.getString(R.string.climbing)
                    "Soccer" -> context.getString(R.string.soccer)
                    "Chess" -> context.getString(R.string.chess)
                    "Checkers" -> context.getString(R.string.checkers)
                    else -> ""
                }
            )
        }

        return list.joinToString()
    }

    override fun getServerList(context: Context, answerLine: String?): MutableList<String>? {
        val serverList: MutableList<String> = mutableListOf()
        if (answerLine.isNullOrEmpty()) return null

        answerLine.toList()?.forEach {
            serverList.add(
                when (it) {
                    context.getString(R.string.badminton) -> "Badminton"
                    context.getString(R.string.basketball) -> "Basketball"
                    context.getString(R.string.baseball) -> "Baseball"
                    context.getString(R.string.billiards) -> "Billiards"
                    context.getString(R.string.boxing) -> "Boxing"
                    context.getString(R.string.wrestling) -> "Wrestling"
                    context.getString(R.string.bowling) -> "Bowling"
                    context.getString(R.string.cycling) -> "Cycling"
                    context.getString(R.string.volleyball) -> "Volleyball"
                    context.getString(R.string.gymnastics) -> "Gymnastics"
                    context.getString(R.string.golf) -> "Golf"
                    context.getString(R.string.rowing) -> "Rowing"
                    context.getString(R.string.darts) -> "Darts"
                    context.getString(R.string.skating) -> "Skating"
                    context.getString(R.string.karate) -> "Karate"
                    context.getString(R.string.tennis) -> "Tennis"
                    context.getString(R.string.swimming) -> "Swimming"
                    context.getString(R.string.judo) -> "Judo"
                    context.getString(R.string.climbing) -> "Climbing"
                    context.getString(R.string.soccer) -> "Soccer"
                    context.getString(R.string.chess) -> "Chess"
                    context.getString(R.string.checkers) -> "Checkers"
                    else -> ""
                }
            )
        }

        return serverList
    }
}