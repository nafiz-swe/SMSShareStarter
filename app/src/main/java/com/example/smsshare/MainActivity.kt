package com.example.smsshare

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.text.InputType
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(20))
            gravity = Gravity.TOP
        }
        setContentView(ScrollView(this).apply { addView(root) })
        if (BuildConfig.APP_ROLE == "user") showUser() else showAdmin()
    }

    private fun title(s: String) {
        root.addView(TextView(this).apply {
            text = s; textSize = 24f; typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 0, 0, dp(12))
        })
    }
    private fun field(hint: String, multi: Boolean = false): EditText {
        val e = EditText(this).apply {
            this.hint = hint
            textSize = 16f
            setPadding(dp(12), dp(8), dp(12), dp(8))
            if (multi) {
                minLines = 4
                gravity = Gravity.TOP
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
        }
        root.addView(e, LinearLayout.LayoutParams(-1, if (multi) dp(130) else dp(54)).apply {
            bottomMargin = dp(10)
        })
        return e
    }
    private fun button(label: String, action: () -> Unit) {
        root.addView(Button(this).apply { text = label; setOnClickListener { action() } },
            LinearLayout.LayoutParams(-1, dp(52)).apply { bottomMargin = dp(10) })
    }
    private fun status(s: String) {
        root.addView(TextView(this).apply { text = s; textSize = 14f; setPadding(0, dp(8), 0, dp(8)) })
    }

    private fun showUser() {
        title("SMS Share")
        status("Share only a message you choose. This app does not read your inbox.")
        val userId = field("Your registered User ID / phone number")
        val sender = field("SMS sender / phone number")
        val message = field("Paste the SMS you want to share", true)
        val consent = CheckBox(this).apply {
            text = "I choose to send this message to the administrator."
            setPadding(0, 0, 0, dp(12))
        }
        root.addView(consent)
        button("Share to Admin") {
            val uid = userId.text.toString().trim()
            val from = sender.text.toString().trim()
            val body = message.text.toString().trim()
            when {
                uid.isBlank() || from.isBlank() || body.isBlank() ->
                    status("Please fill in your User ID, sender number, and message.")
                !consent.isChecked -> status("Please confirm consent before sharing.")
                else -> {
                    // API integration intentionally requires a real authenticated backend.
                    // Do not send personal SMS to an unverified/public endpoint.
                    status("Ready to share. Configure your authenticated HTTPS backend in the project before sending real messages.")
                }
            }
        }
        button("Clear message") { message.setText(""); sender.setText(""); consent.isChecked = false }
    }

    private fun showAdmin() {
        title("SMS Share — Admin")
        status("Admin inbox scaffold. Connect an authenticated backend to view messages from multiple registered users.")
        val search = field("Search by user ID or sender number")
        button("Load shared messages") {
            status("Backend is not configured. Set API_BASE_URL and implement admin authentication/API before using this screen.")
        }
        status("Security note: admin access must be enforced by the server, not only by hiding screens in the APK.")
    }
}
