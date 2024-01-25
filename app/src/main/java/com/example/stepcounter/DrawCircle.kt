package com.example.stepcounter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stepcounter.ui.theme.StepCounterTheme
import com.example.stepcounter.ui.theme.colorBehind
import com.example.stepcounter.ui.theme.colorDone
import com.example.stepcounter.ui.theme.colorInProcess

@Composable
fun DrawCircle(degreesPassed: Float = 350f, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .sizeIn(1.dp, 30.dp)
//        .fillMaxSize()
//        .background(colorBehind)
        .padding(30.dp)
        .aspectRatio(1f)


    ){
//        100.dp.toPx()
        val style2 = with(LocalDensity.current) {
            Stroke(
                100.dp.toPx()
            )

        }


//        class Stroke(
//            val width: Float = 0.0f,
//            val miter: Float = androidx.compose.ui.graphics.drawscope.Stroke.DefaultMiter,
//            val cap: StrokeCap = androidx.compose.ui.graphics.drawscope.Stroke.DefaultCap,
//            val join: StrokeJoin = androidx.compose.ui.graphics.drawscope.Stroke.DefaultJoin,
//            val pathEffect: PathEffect? = null
//        ) : DrawStyle() {

        val circleSize = 200.dp // Adjust the size as needed


        val style = Stroke(
            width = 50f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            miter = 30f
        )

//        Spacer(modifier = Modifier
//            .fillMaxSize()
//            .drawBehind {
//                drawCircle(Color.Magenta)
//
//            })

        Canvas(
            modifier = Modifier
                .fillMaxSize()
            ,
            onDraw = {

                drawArc(
                    color = colorBehind,
                    startAngle = 90f,
                    sweepAngle = 360f,
                    topLeft = Offset.Zero,
                    size = size,
                    useCenter = false,
                    style = style
                )

                drawArc(
                    color = if (degreesPassed < 360) colorInProcess else colorDone,
                    startAngle = 90f,
                    sweepAngle = degreesPassed,
                    topLeft = Offset.Zero,
                    size = size,
                    useCenter = false,
                    style = style
                    )


            })

    }

}



@Preview(showBackground = true)
@Composable
fun DrawCirclePreview() {
    StepCounterTheme {
        DrawCircle()
    }
}