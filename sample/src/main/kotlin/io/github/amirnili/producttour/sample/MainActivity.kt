package io.github.amirnili.producttour.sample

import android.os.Bundle
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.util.Locale
import io.github.amirnili.producttour.sample.R
import com.example.compose.producttour.core.model.SpotlightShape
import com.example.compose.producttour.core.model.TourStep
import com.example.compose.producttour.material.MaterialTourTooltip
import com.example.compose.producttour.material.MaterialTourTooltipStyle
import com.example.compose.producttour.ui.ProductTour
import com.example.compose.producttour.ui.ProductTourOverlayStyle
import com.example.compose.producttour.ui.ProductTourUiConfig
import com.example.compose.producttour.ui.TourLabels
import com.example.compose.producttour.ui.rememberProductTourState
import com.example.compose.producttour.ui.target.tourTarget

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SampleScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SampleScreen() {
    var isPersian by remember { mutableStateOf(false) }
    val steps = remember(isPersian) { sampleTourSteps() }
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    val state = rememberProductTourState()
    val baseContext = LocalContext.current
    val baseConfiguration = LocalConfiguration.current
    val localizedContext = remember(isPersian, baseContext, baseConfiguration) {
        val configuration = Configuration(baseConfiguration)
        configuration.setLocale(if (isPersian) Locale.forLanguageTag("fa") else Locale.ENGLISH)
        baseContext.createConfigurationContext(configuration)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr,
    ) {
        MaterialTheme {
            ProductTour(
                state = state,
                steps = steps,
                config = ProductTourUiConfig(
                    showProgress = true,
                    overlayStyle = ProductTourOverlayStyle(
                        spotlightBorderColor = MaterialTheme.colorScheme.primary,
                        spotlightBorderWidth = 3f,
                    ),
                    labels = TourLabels(
                        next = stringResource(R.string.tour_next),
                        previous = stringResource(R.string.tour_previous),
                        skip = stringResource(R.string.tour_skip),
                        finish = stringResource(R.string.tour_finish),
                    ),
                ),
                tooltipContent = { step, actions, labels, progress ->
                    MaterialTourTooltip(
                        step = step,
                        actions = actions,
                        labels = labels,
                        progress = progress,
                        modifier = Modifier.widthIn(max = 360.dp),
                        style = MaterialTourTooltipStyle(
                            primaryButtonContainerColor = MaterialTheme.colorScheme.primary,
                            primaryButtonContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                },
            ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(R.string.screen_title))
                        },
                        actions = {
                            OutlinedButton(onClick = { isPersian = !isPersian }) {
                                Text(stringResource(R.string.language_toggle))
                            }
                        },
                    )
                },
                bottomBar = {
                    NavigationBar(modifier = Modifier.tourTarget("navigation")) {
                        val navigationLabels = if (isPersian) {
                            listOf(
                                stringResource(R.string.nav_home),
                                stringResource(R.string.nav_saved),
                                stringResource(R.string.nav_profile),
                            )
                        } else {
                            listOf(
                                stringResource(R.string.nav_home),
                                stringResource(R.string.nav_saved),
                                stringResource(R.string.nav_profile),
                            )
                        }
                        navigationLabels.forEachIndexed { index, label ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = { Text(label.take(1)) },
                                label = { Text(label) },
                            )
                        }
                    }
                },
            ) { padding ->
                HomeContent(
                    padding = padding,
                    query = query,
                    onQueryChange = { query = it },
                    onStartTour = { state.start() },
                    onRestartTour = {
                        state.updateSteps(steps)
                        state.start()
                    },
                )
            }
        }
    }
    }
}

private fun sampleTourSteps(): List<TourStep> {
    return listOf(
            TourStep(
                id = "search",
                titleResId = R.string.tour_search_title,
                descriptionResId = R.string.tour_search_description,
                spotlightShape = SpotlightShape.Rounded(20f),
                padding = 1f,
            ),
            TourStep(
                id = "card",
                titleResId = R.string.tour_card_title,
                descriptionResId = R.string.tour_card_description,
                spotlightShape = SpotlightShape.RoundedRectangle,
            ),
            TourStep(
                id = "action",
                titleResId = R.string.tour_action_title,
                descriptionResId = R.string.tour_action_description,
                spotlightShape = SpotlightShape.Rounded(20f),
            ),
            TourStep(
                id = "navigation",
                titleResId = R.string.tour_navigation_title,
                descriptionResId = R.string.tour_navigation_description,
                spotlightShape = SpotlightShape.RoundedRectangle,
            ),
        )
}

@Composable
private fun HomeContent(
    padding: PaddingValues,
    query: String,
    onQueryChange: (String) -> Unit,
    onStartTour: () -> Unit,
    onRestartTour: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f).tourTarget("search"),
                shape =  RoundedCornerShape(12.0.dp),
                singleLine = true,
                label = { Text(stringResource(R.string.search_label)) },
            )
            Button(onClick = onStartTour) {
                Text(stringResource(R.string.start_tour))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .tourTarget("card"),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    stringResource(R.string.featured_workspace),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    stringResource(R.string.sample_description),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .tourTarget("action"),
            onClick = onRestartTour,
        ) {
            Text(stringResource(R.string.restart_tour))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRestartTour,
        ) {
            Text(stringResource(R.string.replay_scenario))
        }
    }
}
