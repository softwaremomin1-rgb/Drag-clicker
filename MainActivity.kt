package com.example.volumemapper

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var xEdit: EditText
    private lateinit var yEdit: EditText
    private lateinit var intervalEdit: EditText
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (20 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }

        fun label(text: String) = TextView(this).apply {
            this.text = text
            textSize = 16f
            setPadding(0, 12, 0, 6)
        }

        val title = TextView(this).apply {
            text = "Volume Mapper V2"
            textSize = 26f
            gravity = Gravity.CENTER_HORIZONTAL
        }
        root.addView(title, ViewGroup.LayoutParams(-1, -2))

        val info = TextView(this).apply {
            text = "For Redmi 9 Power • MIUI 14 / Android 12\\n\\nVolume Down = toggle repeated tap in Minecraft.\\nThis is an accessibility-based mapper; Xiaomi/Android may restrict hardware-key filtering."
            textSize = 15f
        }
        root.addView(info, ViewGroup.LayoutParams(-1, -2))

        root.addView(label("Tap X (screen pixels)"))
        xEdit = EditText(this).apply {
            hint = "e.g. 540"
            inputType = 2
            setText("540")
        }
        root.addView(xEdit, ViewGroup.LayoutParams(-1, -2))

        root.addView(label("Tap Y (screen pixels)"))
        yEdit = EditText(this).apply {
            hint = "e.g. 900"
            inputType = 2
            setText("900")
        }
        root.addView(yEdit, ViewGroup.LayoutParams(-1, -2))

        root.addView(label("Repeat interval (ms)"))
        intervalEdit = EditText(this).apply {
            hint = "50–200"
            inputType = 2
            setText("80")
        }
        root.addView(intervalEdit, ViewGroup.LayoutParams(-1, -2))

        val saveButton = Button(this).apply {
            text = "Save settings"
            setOnClickListener { saveSettings() }
        }
        root.addView(saveButton)

        val accessibilityButton = Button(this).apply {
            text = "Open Accessibility settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
        root.addView(accessibilityButton)

        val testButton = Button(this).apply {
            text = "Test one tap"
            setOnClickListener {
                saveSettings()
                MapperAccessibilityService.instance?.testOneTap()
                    ?: Toast.makeText(this@MainActivity, "Enable Volume Mapper in Accessibility first.", Toast.LENGTH_LONG).show()
            }
        }
        root.addView(testButton)

        val toggleButton = Button(this).apply {
            text = "Start / Stop mapping"
            setOnClickListener {
                saveSettings()
                MapperAccessibilityService.instance?.toggleRepeating()
                    ?: Toast.makeText(this@MainActivity, "Enable Volume Mapper in Accessibility first.", Toast.LENGTH_LONG).show()
            }
        }
        root.addView(toggleButton)

        statusText = TextView(this).apply {
            textSize = 16f
            setPadding(0, 18, 0, 0)
            text = "Status: waiting for Accessibility Service"
        }
        root.addView(statusText)

        val warning = TextView(this).apply {
            text = "\\nMinecraft package: com.mojang.minecraftpe\\n\\nUse only where automation is allowed. Do not use this to bypass anti-cheat or server rules."
            textSize = 13f
        }
        root.addView(warning)

        setContentView(root)
    }

    private fun saveSettings() {
        val x = xEdit.text.toString().toIntOrNull()?.coerceAtLeast(0) ?: 540
        val y = yEdit.text.toString().toIntOrNull()?.coerceAtLeast(0) ?: 900
        val interval = intervalEdit.text.toString().toLongOrNull()?.coerceIn(30L, 2000L) ?: 80L

        getSharedPreferences("mapper", MODE_PRIVATE).edit()
            .putInt("x", x)
            .putInt("y", y)
            .putLong("interval", interval)
            .apply()

        Toast.makeText(this, "Saved: ($x, $y), ${interval}ms", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        statusText.text = if (MapperAccessibilityService.instance != null) {
            "Status: Accessibility Service connected"
        } else {
            "Status: Accessibility Service not connected"
        }
    }
}
