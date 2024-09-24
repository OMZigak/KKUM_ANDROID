package com.teamkkumul.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.teamkkumul.core.designsystem.R

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))

@Stable
class KkumulTypography internal constructor(
    title00: TextStyle,
    title01: TextStyle,
    title02: TextStyle,
    title03: TextStyle,
    head01: TextStyle,
    head02: TextStyle,
    body01: TextStyle,
    body02: TextStyle,
    body03: TextStyle,
    body04: TextStyle,
    body05: TextStyle,
    body06: TextStyle,
    caption01: TextStyle,
    caption02: TextStyle,
    label00: TextStyle,
    label01: TextStyle,
    label02: TextStyle,
) {
    var title00: TextStyle by mutableStateOf(title00)
        private set
    var title01: TextStyle by mutableStateOf(title01)
        private set
    var title02: TextStyle by mutableStateOf(title02)
        private set
    var title03: TextStyle by mutableStateOf(title03)
        private set
    var head01: TextStyle by mutableStateOf(head01)
        private set
    var head02: TextStyle by mutableStateOf(head02)
        private set
    var body01: TextStyle by mutableStateOf(body01)
        private set
    var body02: TextStyle by mutableStateOf(body02)
        private set
    var body03: TextStyle by mutableStateOf(body03)
        private set
    var body04: TextStyle by mutableStateOf(body04)
        private set
    var body05: TextStyle by mutableStateOf(body05)
        private set
    var body06: TextStyle by mutableStateOf(body06)
        private set
    var caption01: TextStyle by mutableStateOf(caption01)
        private set
    var caption02: TextStyle by mutableStateOf(caption02)
        private set
    var label00: TextStyle by mutableStateOf(label00)
        private set
    var label01: TextStyle by mutableStateOf(label01)
        private set
    var label02: TextStyle by mutableStateOf(label02)
        private set

    fun copy(
        title00: TextStyle = this.title00,
        title01: TextStyle = this.title01,
        title02: TextStyle = this.title02,
        title03: TextStyle = this.title03,
        head01: TextStyle = this.head01,
        head02: TextStyle = this.head02,
        body01: TextStyle = this.body01,
        body02: TextStyle = this.body02,
        body03: TextStyle = this.body03,
        body04: TextStyle = this.body04,
        body05: TextStyle = this.body05,
        body06: TextStyle = this.body06,
        caption01: TextStyle = this.caption01,
        caption02: TextStyle = this.caption02,
        label00: TextStyle = this.label00,
        label01: TextStyle = this.label01,
        label02: TextStyle = this.label02,
    ): KkumulTypography = KkumulTypography(
        title00,
        title01,
        title02,
        title03,
        head01,
        head02,
        body01,
        body02,
        body03,
        body04,
        body05,
        body06,
        caption01,
        caption02,
        label00,
        label01,
        label02,
    )

    fun update(other: KkumulTypography) {
        title00 = other.title00
        title01 = other.title01
        title02 = other.title02
        title03 = other.title03
        head01 = other.head01
        head02 = other.head02
        body01 = other.body01
        body02 = other.body02
        body03 = other.body03
        body04 = other.body04
        body05 = other.body05
        body06 = other.body06
        caption01 = other.caption01
        caption02 = other.caption02
        label00 = other.label00
        label01 = other.label01
        label02 = other.label02
    }
}

@Composable
fun kkumulTypography(): KkumulTypography {
    return KkumulTypography(
        title00 = TextStyle(
            fontFamily = PretendardBold,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 38.4.sp,
            letterSpacing = (-0.02).em,
        ),
        title01 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 38.4.sp,
            letterSpacing = (-0.02).em,
        ),
        title02 = TextStyle(
            fontFamily = PretendardMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 38.4.sp,
            letterSpacing = (-0.02).em,
        ),
        title03 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 38.4.sp,
            letterSpacing = (-0.02).em,
        ),
        head01 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 35.2.sp,
            letterSpacing = (-0.02).em,
        ),
        head02 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 35.2.sp,
            letterSpacing = (-0.02).em,
        ),
        body01 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 28.8.sp,
            letterSpacing = (-0.02).em,
        ),
        body02 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 28.8.sp,
            letterSpacing = (-0.02).em,
        ),
        body03 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 25.6.sp,
            letterSpacing = (-0.02).em,
        ),
        body04 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 25.6.sp,
            letterSpacing = (-0.02).em,
        ),
        body05 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 22.4.sp,
            letterSpacing = (-0.02).em,
        ),
        body06 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.4.sp,
            letterSpacing = (-0.02).em,
        ),
        caption01 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 19.2.sp,
            letterSpacing = (-0.02).em,
        ),
        caption02 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 19.2.sp,
            letterSpacing = (-0.02).em,
        ),
        label00 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.6.sp,
            letterSpacing = (-0.02).em,
        ),
        label01 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.02).em,
        ),
        label02 = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.02).em,
        ),
    )
}
