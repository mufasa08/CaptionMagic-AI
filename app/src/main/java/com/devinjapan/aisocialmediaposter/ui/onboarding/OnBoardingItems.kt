package com.devinjapan.aisocialmediaposter.ui.onboarding

import com.devinjapan.aisocialmediaposter.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    R.drawable.ic_magic_wand,
                    R.string.onboarding_title_1,
                    R.string.onboarding_text_1
                ),
                OnBoardingItems(
                    R.drawable.ic_magic_wand,
                    R.string.onboarding_title_2,
                    R.string.onboarding_text_2
                ),
                OnBoardingItems(
                    R.drawable.ic_magic_wand,
                    R.string.onboarding_title_3,
                    R.string.onboarding_text_3
                )
            )
        }
    }
}
