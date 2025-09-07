package com.spp.android.myapplication.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

enum class HomeTab(val title: String) { Profile("My Profile"), Contacts("My Contacts") }

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    profile: @Composable () -> Unit,
    contacts: @Composable () -> Unit
) {
    val tabs = HomeTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(tab.title) }
                )
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