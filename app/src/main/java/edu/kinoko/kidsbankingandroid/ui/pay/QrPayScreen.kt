package edu.kinoko.kidsbankingandroid.ui.pay

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.TransactionType
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.pay.component.ScannerOverlay
import edu.kinoko.kidsbankingandroid.ui.pay.util.QrAnalyzer
import edu.kinoko.kidsbankingandroid.ui.pay.util.RoiPercents
import edu.kinoko.kidsbankingandroid.ui.pay.util.vibrateScanSuccess
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Composable
fun QrPayScreen(
    nav: NavHostController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()

    val roi = remember { RoiPercents(left = 0.15f, top = 0.25f, right = 0.85f, bottom = 0.60f) }
    var handled by remember { mutableStateOf(false) }

    val previewView = remember {
        PreviewView(context).apply {
            // На некоторых устройствах (Xiaomi, старые Samsung) только COMPATIBLE спасает от чёрного экрана
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // --- Рантайм-разрешение ---
    var hasPermission by remember { mutableStateOf(false) }
    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        // если уже выдано — отлично; иначе попросим
        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        hasPermission = granted
        if (!granted) requestPermission.launch(Manifest.permission.CAMERA)
    }

    // --- Старт камеры, когда есть разрешение ---
    DisposableEffect(hasPermission) {
        if (!hasPermission) {
            onDispose { /* ничего */ }
            return@DisposableEffect onDispose { }
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = androidx.core.content.ContextCompat.getMainExecutor(context)
        val analyzerExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val analyzer = QrAnalyzer(
                scanner = BarcodeScanning.getClient(
                    BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build()
                ),
                roi = roi,
                onQr = { value ->
                    if (handled) return@QrAnalyzer
                    handled = true
                    scope.launch {
                        context.vibrateScanSuccess()
                    }
                    nav.navigate(
                        "${AppRoutes.TRANSACTION_SPLASH}?type=${TransactionType.QR_PAY.name}&to=${value.shopId}&sum=${value.sum}"
                    ) {
                        launchSingleTop = true
                    }
                },
                requireStableMs = 600L,
            )
            analysis.setAnalyzer(analyzerExecutor, analyzer)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            } catch (e: Exception) {
                Log.e("CameraX", "bind failed", e)
            }
        }, executor)

        onDispose {
            val provider = ProcessCameraProvider.getInstance(context)
            try { provider.get().unbindAll() } catch (_: Exception) {}
            analyzerExecutor.shutdown()
        }
    }

    // --- UI поверх ---
    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        ScannerOverlay(roi = roi)
        Box(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomButton(
                text = "Назад",
                onClick = {
                    nav.navigate(AppRoutes.HOME) {
                        popUpTo(nav.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
