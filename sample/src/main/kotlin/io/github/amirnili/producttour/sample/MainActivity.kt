package io.github.amirnili.producttour.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.producttour.core.model.SpotlightShape
import com.example.compose.producttour.core.model.TourStep
import com.example.compose.producttour.material.MaterialTourTooltip
import com.example.compose.producttour.ui.ProductTour
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
    val steps = remember {
        listOf(
            TourStep(
                id = "search",
                title = "Search",
                description = "Start here to search your workspace.",
                spotlightShape = SpotlightShape.Circle,
                padding = 10f,
            ),
            TourStep(
                id = "card",
                title = "Featured card",
                description = "Important content can be highlighted with a rounded rectangle.",
                spotlightShape = SpotlightShape.RoundedRectangle,
            ),
            TourStep(
                id = "action",
                title = "Primary action",
                description = "Use the action button to complete the flow.",
                spotlightShape = SpotlightShape.Rounded(20f),
            ),
            TourStep(
                id = "navigation",
                title = "Navigation",
                description = "The bottom navigation remains available throughout the tour.",
                spotlightShape = SpotlightShape.RoundedRectangle,
            ),
        )
    }
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    val state = rememberProductTourState()

    MaterialTheme {
        ProductTour(
            state = state,
            steps = steps,
            config = ProductTourUiConfig(
                showProgress = true,
                spotlightBorderColor = MaterialTheme.colorScheme.primary,
                spotlightBorderWidth = 3f,
                labels = TourLabels(
                    next = "Next",
                    previous = "Previous",
                    skip = "Skip",
                    finish = "Finish",
                ),
            ),
            tooltipContent = { step, actions, labels, progress ->
                MaterialTourTooltip(
                    step = step,
                    actions = actions,
                    labels = labels,
                    progress = progress,
                    modifier = Modifier.widthIn(max = 360.dp),
                )
            },
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Compose Product Tour") },
                    )
                },
                bottomBar = {
                    NavigationBar(modifier = Modifier.tourTarget("navigation")) {
                        listOf("Home", "Saved", "Profile").forEachIndexed { index, label ->
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
                singleLine = true,
                label = { Text("Search") },
            )
            Button(onClick = onStartTour) {
                Text("Start Tour")
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .tourTarget("card"),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Featured workspace", style = MaterialTheme.typography.titleLarge)
                Text(
                    "A compact sample screen for manual tour validation.",
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
            Text("Restart Tour")
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRestartTour,
        ) {
            Text("Replay current scenario")
        }
    }
}
