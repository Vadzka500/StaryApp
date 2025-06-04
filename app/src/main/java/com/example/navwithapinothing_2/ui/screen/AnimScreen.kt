package com.example.navwithapinothing_2.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.navwithapinothing_2.R



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AnimScreen(modifier: Modifier = Modifier) {

    Box(modifier = Modifier.fillMaxSize()) {
        var isVisibleBox by remember {
            mutableStateOf(false)
        }

        var isVisibleBox_2 by remember {
            mutableStateOf(false)
        }

        var isShadow by remember {
            mutableStateOf(false)
        }

        val shadow = animateDpAsState(
            targetValue = if (isShadow) {
                32.dp
            } else {
                0.dp
            }, label = ""
        )

        var rotateIcon by remember {
            mutableStateOf(false)
        }


        AnimatedVisibility(
            visible = isVisibleBox,
            enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()
        ) {

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(50.dp)
                    .height(150.dp)
                    .align(Alignment.TopCenter)

                    .graphicsLayer {
                        shadowElevation = shadow.value.toPx()
                    }
                    //.clip(RoundedCornerShape(30.dp))
                    .background(Color.Green)


            )

        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { isVisibleBox = !isVisibleBox },
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(text = "Go")
        }

        Button(
            onClick = { isShadow = !isShadow },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 150.dp)
        ) {
            Text(text = "Go 2")
        }

        Button(
            onClick = { isShadow = !isShadow },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 250.dp)
        ) {
            Text(text = "Go 4")
        }

        Button(
            onClick = { rotateIcon = !rotateIcon },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 150.dp)
        ) {
            Text(text = "Go 3")
        }

        Button(
            onClick = { isVisibleBox_2 = !isVisibleBox_2 },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 100.dp)
        ) {
            Text(text = "Go")
        }

        /*   val rotate by rememberInfiniteTransition(label = "").animateFloat(
               ini
           )*/

        val yAnimation = remember { Animatable(0f) }

        /*val rotate by animateFloatAsState(
            targetValue = if (rotateIcon) 50f else 0f,
            animationSpec = tween(500)
        )*/

        /* if(rotateIcon){
             LaunchedEffect("rot") {
                 yAnimation.animateTo(50f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                 yAnimation.animateTo(-50f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                 yAnimation.animateTo(50f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                 yAnimation.animateTo(0f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))

             }
         }*/

        if (rotateIcon) {
            LaunchedEffect("rot") {
                yAnimation.animateTo(
                    50f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = 0.1f
                    )
                )
                yAnimation.animateTo(
                    -50f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = 0.1f
                    )
                )
                yAnimation.animateTo(
                    50f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                yAnimation.animateTo(
                    0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

            }
        }





        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.rotate(yAnimation.value)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Black)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            if (isVisibleBox_2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(20.dp)
                        .background(Color.Blue)
                )
            }
        }


    }
    val webView = WebView(LocalContext.current).apply {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        webViewClient = WebViewClient()
    }

    val htmlData = getHTMLData("0IwTYtpRPY0")

    AndroidView(factory = { webView }) { view ->
        view.loadDataWithBaseURL(
            "https://www.youtube.com/embed/",
            htmlData,
            "text/html",
            "UTF-8",
            null
        )
    }


    //YoutubeView(id = "0IwTYtpRPY0", lifecycleOwner = LocalLifecycleOwner.current)


}

@Composable
fun YoutubeView(modifier: Modifier = Modifier, id: String, lifecycleOwner: LifecycleOwner) {



}

fun getHTMLData(videoId: String): String {
    return """
        <html>
        
            <body style="margin:0px;padding:0px;">
               <div id="player"></div>
                <script>
                    var player;
                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                            height: '450',
                            width: '750',
                            videoId: '$videoId',
                            playerVars: {
                                'playsinline': 1,
                                'controls': 0,
                                'showinfo': 0,
                                'rel': 0,
                                'iv_load_policy': 3
                            },
                            events: {
                                'onReady': onPlayerReady
                            }
                        });
                    }
                    function onPlayerReady(event) {
                   
                    }
                   
                </script>
                <script src="https://www.youtube.com/iframe_api"></script>
            </body>
        </html>
    """.trimIndent()
}