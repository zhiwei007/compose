package com.example.composeviewpager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composeviewpager.ui.theme.ComposeViewpagerTheme
import com.example.composeviewpager.ui.theme.lerp
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeViewpagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    HorizontalPagerIndicatorSample()
                    HorizontalPagerWithOffsetTransition()
                }
            }
        }
    }
}

/*@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Sample() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("horiz_pager_with_indicator_title") },
                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)) {
            val pagerState = rememberPagerState()

            // Display 10 items
            HorizontalPager(
                count = 10,
                state = pagerState,
                // Add 32.dp horizontal padding to 'center' the pages
                contentPadding = PaddingValues(horizontal = 32.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                PagerSampleItem(
                    page = page,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )

            ActionsRow(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}*/

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerIndicatorSample() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("horiz_pager_with_indicator_title") },
                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        modifier = Modifier.fillMaxSize()
    ){ padding ->
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(padding)){
            val pagerState = rememberPagerState()

            // Display 10 items
            HorizontalPager(
                count = 15,
                state = pagerState,
                // Add 32.dp horizontal padding to 'center' the pages
                contentPadding = PaddingValues(horizontal = 32.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                Card(shape = MaterialTheme.shapes.small, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)){
                    Box(modifier = Modifier.fillMaxSize(fraction = 0.95f).background(Color.Gray)){
                        Text(text = "$page",  modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp))
                    }
                }
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                activeColor = Color.Magenta
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
    HorizontalPager(
        count = 10,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = 32.dp),
        modifier = modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box {
                Image(
                    painter = rememberImagePainter(
                        data = rememberRandomSampleImageUrl(width = 500),
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )

                ProfilePicture(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        // We add an offset lambda, to apply a light parallax effect
                        .offset {
                            // Calculate the offset for the current page from the
                            // scroll position
                            val pageOffset =
                                this@HorizontalPager.calculateCurrentOffsetForPage(page)
                            // Then use it as a multiplier to apply an offset
                            IntOffset(
                                x = (36.dp * pageOffset).roundToPx(),
                                y = 30
                            )
                        }
                )
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ProfilePicture(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colors.surface)
    ) {
        Image(
            painter = rememberImagePainter(rememberRandomSampleImageUrl()),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
    }
}