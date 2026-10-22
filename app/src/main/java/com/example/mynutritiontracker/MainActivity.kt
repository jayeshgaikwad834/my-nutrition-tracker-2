package com.example.mynutritiontracker

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("tracker", MODE_PRIVATE)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(24))
        }
        val scroll = ScrollView(this).apply { addView(page) }
        val title = TextView(this).apply {
            text = "My Nutrition Tracker"
            textSize = 26f
            setTypeface(null, Typeface.BOLD)
        }
        page.addView(title)
        page.addView(TextView(this).apply {
            text = "Offline nutrition and walking log"
            textSize = 15f
            setPadding(0, dp(4), 0, dp(20))
        })
        fun label(text: String) {
            page.addView(TextView(this).apply {
                this.text = text
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setPadding(0, dp(12), 0, dp(6))
            })
        }
        fun field(hint: String, key: String): EditText {
            return EditText(this).apply {
                this.hint = hint
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                setText(prefs.getString(key, ""))
                page.addView(this, ViewGroup.LayoutParams(-1, dp(52)))
            }
        }
        label("Nutrition")
        val food = EditText(this).apply {
            hint = "Food / meal (e.g. poha, lunch)"
            setText(prefs.getString("food", ""))
            page.addView(this, ViewGroup.LayoutParams(-1, dp(52)))
        }
        val calories = field("Calories (kcal)", "calories")
        val protein = field("Protein (grams)", "protein")
        label("Walking")
        val morning = field("Morning walk minutes (target 30)", "morning")
        val night = field("Night walk minutes (target 15)", "night")
        val steps = field("Steps (optional)", "steps")
        val save = Button(this).apply {
            text = "Save Today’s Entry"
            setOnClickListener {
                prefs.edit()
                    .putString("food", food.text.toString())
                    .putString("calories", calories.text.toString())
                    .putString("protein", protein.text.toString())
                    .putString("morning", morning.text.toString())
                    .putString("night", night.text.toString())
                    .putString("steps", steps.text.toString())
                    .apply()
                Toast.makeText(this@MainActivity, "Saved on this device", Toast.LENGTH_SHORT).show()
            }
        }
        page.addView(save, ViewGroup.LayoutParams(-1, dp(52)))
        page.addView(TextView(this).apply {
            text = "Your entries are stored locally on this device. No account or internet connection is required."
            textSize = 13f
            setPadding(0, dp(14), 0, 0)
        })
        setContentView(scroll)
    }
}
