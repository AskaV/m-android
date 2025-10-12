package com.spp.android.myapplication.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

enum class HomeTab(@StringRes val titleRes: Int) {
    Profile(AppText.HomeTabs.PROFILE.res), Contacts(AppText.HomeTabs.CONTACTS.res)
}

class HomeTabsController {
    internal var jumpTo: ((HomeTab) -> Unit)? = null
    fun goTo(tab: HomeTab) {
        jumpTo?.invoke(tab)
    }
}

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    profile: @Composable () -> Unit = {},
    contacts: @Composable () -> Unit = {},
    controller: HomeTabsController? = null
) {
    val tabs = HomeTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(controller) {
        controller?.jumpTo = { tab ->
            scope.launch { pagerState.animateScrollToPage(tab.ordinal) }
        }
    }

    Column(modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(stringResource(tab.titleRes)) })
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (tabs[page]) {
                HomeTab.Profile -> profile()
                HomeTab.Contacts -> contacts()
            }
        }
    }
}