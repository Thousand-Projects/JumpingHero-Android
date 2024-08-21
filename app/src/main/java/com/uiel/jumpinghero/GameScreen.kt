package com.uiel.jumpinghero

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun GameScreen(modifier: Modifier = Modifier) {

    val playerY = remember { mutableStateOf(0f) }
    val obstacleX = remember { mutableStateOf(1f) }
    val jumpVelocity = remember { mutableStateOf(0f) }
    val isJumping = remember { mutableStateOf(false) }
    val difficultyLevel = remember { mutableStateOf(1) }
    val obstacleSpeed = remember { mutableStateOf(0.02f) }
    val obstacleSpawnInterval = remember { mutableStateOf(3000L) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            // 캐릭터 점프 로직
            if (isJumping.value) {
                playerY.value += jumpVelocity.value
                jumpVelocity.value += 1f
                if (playerY.value >= 0f) {
                    playerY.value = 0f
                    isJumping.value = false
                }
            }
            // 장애물 이동 로직
            obstacleX.value -= obstacleSpeed.value
            if (obstacleX.value < -0.1f) {
                obstacleX.value = 1f
                difficultyLevel.value += 1

                // 난이도에 따른 속도 및 생성 간격 조정
                obstacleSpeed.value += 0.002f // 장애물 속도 증가
                if (obstacleSpawnInterval.value > 1000L) {
                    obstacleSpawnInterval.value -= 200L // 장애물 생성 간격 감소
                }
            }
        }
    }

    // 게임 화면 그리기
    Surface(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {
            // 캐릭터 그리기
            translate(left = size.width * 0.1f, top = size.height * 0.8f + playerY.value) {
                drawRoundRect(
                    color = Color.Red,
                    size = size * 0.1f,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero
                )
            }

            // 장애물 그리기
            translate(left = size.width * obstacleX.value, top = size.height * 0.8f) {
                drawRoundRect(
                    color = Color.Green,
                    size = size * 0.1f,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero
                )
            }
        }
    }

    // 점프 이벤트 처리
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Transparent),
        onClick = {
            if (!isJumping.value) {
                isJumping.value = true
                jumpVelocity.value = -15f
            }
        }
    ) {
        BasicText(
            text = "Tap to Jump",
            modifier = Modifier.fillMaxWidth(),
            style = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )
    }
}