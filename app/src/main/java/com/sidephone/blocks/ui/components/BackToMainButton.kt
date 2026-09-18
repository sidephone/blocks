package com.sidephone.blocks.ui.components


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sidephone.blocks.R
import com.sidephone.blocks.ui.modifiers.gamepadClickableButton
import com.sidephone.blocks.ui.theme.Dimens

@Composable
fun BackToMainButton(onBack: () -> Unit) {
	MenuButton(
		onClick = onBack,
		text = R.string.main_back,
		modifier = Modifier
			.padding(top = Dimens.MainMenuTitlePaddingBottom)
			.gamepadClickableButton(onBack)
	)
}

