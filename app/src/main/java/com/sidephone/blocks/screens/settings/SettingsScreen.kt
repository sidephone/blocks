package com.sidephone.blocks.screens.settings


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sidephone.blocks.R
import com.sidephone.blocks.settings.Settings
import com.sidephone.blocks.ui.components.BackToMainButton
import com.sidephone.blocks.ui.components.SettingsSwitch
import com.sidephone.blocks.ui.theme.Dimens
import com.sidephone.snake.ui.components.MenuTitle

@Composable
fun SettingsScreen(settings: Settings, onBack: () -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(Dimens.MainMenuButtonContainerPadding),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top
	) {
		MenuTitle(text = stringResource(R.string.main_settings))


		SettingsSwitch(
			titleResId = R.string.setting_ghost_piece,
			summaryOnResId = R.string.setting_ghost_piece_summary_on,
			summaryOffResId = R.string.setting_ghost_piece_summary_off,
			value = settings.ghostPiece(),
			false,
			onValueChange = { settings.setGhostPiece(it) }
		)

		BackToMainButton(onBack = onBack)
	}
}

