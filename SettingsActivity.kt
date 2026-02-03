package com.example.teacherhelper2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etEmail = findViewById<EditText>(R.id.etTeacherEmail)
        val btnSave = findViewById<Button>(R.id.btnSaveSettings)
        val btnShareWithStudent = findViewById<Button>(R.id.btnShareWithStudent)

        val prefs = getSharedPreferences("TeacherPrefs", Context.MODE_PRIVATE)
        etEmail.setText(prefs.getString("saved_email", ""))

        btnSave.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                prefs.edit().putString("saved_email", email).apply()
                Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show()
            }
        }

        btnShareWithStudent.setOnClickListener {
            val savedEmail = etEmail.text.toString().trim()
            if (savedEmail.isEmpty()) {
                Toast.makeText(this, "Сначала введите почту!", Toast.LENGTH_SHORT).show()
            } else {
                // ПРЯМАЯ ССЫЛКА НА ТЕСТ
                val testLink = "https://teacherhelper.app/start?teacher=$savedEmail"

                val shareText = """
                    Привет! Пройди тест по уроку по этой ссылке:
                    $testLink
                    
                    (Если приложение еще не установлено, сначала скачай его здесь: ТВОЯ_ССЫЛКА_НА_САЙТ)
                """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(intent, "Отправить тест ученику:"))
            }
        }
    }
}