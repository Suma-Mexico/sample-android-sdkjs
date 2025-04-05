package com.example.sample_android_sdkjs

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private var permissionRequest: PermissionRequest? = null
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val button: Button = findViewById(R.id.btn_capture)

        // Register permission launcher using the modern Activity Result API
        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    println("Permiso concedido")
                    permissionRequest?.grant(permissionRequest?.resources)
                } else {
                    println("Permiso denegado")
                    Toast.makeText(
                        this, "Se necesita permiso de cámara para continuar", Toast.LENGTH_SHORT
                    ).show()
                    permissionRequest?.deny()
                }

                permissionRequest = null
            }

        // Configure WebView and enable Javascript
        val webView: WebView = findViewById(R.id.webview)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowUniversalAccessFromFileURLs =
            true // Important to allows local JS to make HTTPS requets
        webSettings.mediaPlaybackRequiresUserGesture = false

        // Set WebChromeClient to handle JS console messages and permissions requests
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    val message = it.message()
                    println("WebView Console: $message (Line: ${it.lineNumber()}, Source: ${it.sourceId()})")

                    try {
                        val jsonParse = JSONObject(message)
                        val isError = jsonParse.optString("error")
                        val isFirtsCall: Boolean = jsonParse.optBoolean("firstCall")
                        val image: String? = jsonParse.optString("image")
                        val images: JSONArray? = jsonParse.optJSONArray("images")

                        // Show or hide the capture button based on 'firstCall' flag
                        if (isFirtsCall) {
                            toggleButton(button, true)
                        } else {
                            toggleButton(button, false)
                        }

                        Log.i("Result", "$isError $images $image")
                    } catch (e: Exception) {
                        Log.e("Error WebView Console", "${e.message}")
                    }
                }

                return super.onConsoleMessage(consoleMessage)
            }

            // Handle camera permission request from WebView
            override fun onPermissionRequest(request: PermissionRequest) {
                if (request.resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    //Solicitar el permiso de cámara
                    permissionRequest = request
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }

        // Handle external URL loading behavior (optial override)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                return false
            }
        }

        // Load local HTML file containing the JS logic
        webView.loadUrl("file:///android_asset/www/veridoc.html")

        // Trigger JS function on button click
        button.setOnClickListener {
            webView.evaluateJavascript("(function() { if(window['continueDetection'] && typeof window['continueDetection'] === 'function') {window['continueDetection'](); return 'Success';} else {return 'Failed'; } })();") { result ->
                println(result)
                if (result === "\"Failed\"") {
                    Toast.makeText(this, "Función no encontrada", Toast.LENGTH_SHORT).show()
                } else {
                    toggleButton(button, false)
                }
            }
        }


    }

    // Utility to show or hide the capture button
    fun toggleButton(button: Button, show: Boolean) {
        if (show) {
            button.visibility = View.VISIBLE
        } else {
            button.visibility = View.GONE
        }
    }
}