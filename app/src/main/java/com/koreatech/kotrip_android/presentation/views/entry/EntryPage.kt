package com.koreatech.kotrip_android.presentation.views.entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.components.organisms.EntryCard
import com.koreatech.kotrip_android.presentation.components.organisms.KotripLogoutDialog
import com.koreatech.kotrip_android.presentation.components.organisms.KotripWithdrawDialog

@Composable
fun EntryPage(
    modifier: Modifier = Modifier,
    onClickHistoryStep: () -> Unit,
    onClickScheduleStep: () -> Unit,
    onClickOneDayStep: () -> Unit,
    onWithdraw: () -> Unit,
    onLogout: () -> Unit,
) {
    var withdrawDialogVisible by remember {
        mutableStateOf(false)
    }
    var logoutDialogVisible by remember {
        mutableStateOf(false)
    }
    KotripWithdrawDialog(
        visible = withdrawDialogVisible,
        onDismissRequest = {
            withdrawDialogVisible = false
        },
        onCancel = {
            withdrawDialogVisible = false
        },
        onComplete = {
            withdrawDialogVisible = false
            onWithdraw()
        }
    )
    KotripLogoutDialog(
        visible = logoutDialogVisible,
        onDismissRequest = {
            logoutDialogVisible = false
        },
        onCancel = {
            logoutDialogVisible = false
        },
        onComplete = {
            logoutDialogVisible = true
            onLogout()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Row {
            Text(
                text = "회원탈퇴", modifier = Modifier
                    .padding(6.dp)
                    .clickable(onClick = { withdrawDialogVisible = true })
            )
            Text(text = "|", modifier = Modifier.padding(vertical = 6.dp))
            Text(
                text = "로그아웃", modifier = Modifier
                    .padding(6.dp)
                    .clickable(onClick = { logoutDialogVisible = true })
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EntryCard(
            imageDrawable = R.drawable.img_history,
            title = stringResource(id = R.string.onboard_select_entry_schedule_history_title),
            content = stringResource(id = R.string.onboard_select_entry_schedule_history_content),
            onClick = onClickHistoryStep
        )
        Spacer(modifier = Modifier.height(20.dp))

        EntryCard(
            imageDrawable = R.drawable.img_new_schedule,
            title = stringResource(id = R.string.onboard_select_entry_schedule_title),
            content = stringResource(id = R.string.onboard_select_entry_schedule_content),
            onClick = onClickScheduleStep
        )
        Spacer(modifier = Modifier.height(20.dp))

        EntryCard(
            imageDrawable = R.drawable.img_one_day,
            title = stringResource(id = R.string.onboard_select_entry_schedule_oneday_title),
            content = stringResource(id = R.string.onboard_select_entry_schedule_oneday_content),
            onClick = onClickOneDayStep
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectEntryPagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        EntryPage(
            onClickHistoryStep = {},
            onClickScheduleStep = {},
            onClickOneDayStep = {},
            onWithdraw = {},
            onLogout = {}
        )
    }
}