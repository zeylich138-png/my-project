package com.example.teacherhelper2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class TeacherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        val etEmail = findViewById<EditText>(R.id.etTeacherEmailStorage)
        val btnSave = findViewById<Button>(R.id.btnSaveEmail)
        val btnShareTest = findViewById<Button>(R.id.btnShareApp) // Теперь это "Поделиться тестом"
        val btnTheme = findViewById<Button>(R.id.btnChangeTheme)

        val prefs = getSharedPreferences("TeacherPrefs", Context.MODE_PRIVATE)
        etEmail.setText(prefs.getString("saved_email", ""))

        // 1. Сохранение почты
        btnSave.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.contains("@") && email.contains(".")) {
                prefs.edit().putString("saved_email", email).apply()
                Toast.makeText(this, "Настройки сохранены!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Введите корректный Email!", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Кнопка "ОТПРАВИТЬ ССЫЛКУ УЧЕНИКУ"
        btnShareTest.setOnClickListener {
            val savedEmail = etEmail.text.toString().trim()

            if (savedEmail.isEmpty()) {
                Toast.makeText(this, "Сначала введите и сохраните почту!", Toast.LENGTH_SHORT).show()
            } else {
                // Прямая ссылка, которую перехватит приложение (Deep Link)
                val deepLink = "https://teacherhelper.app/start?teacher=$savedEmail"
                // Ссылка на скачивание (твоя ссылка на Google Диск)
                val downloadLink = "https://drive.google.com/file/d/1xVCypgFv_WLGOMxG1j-7nSg6cr07GbIf/view?usp=sharing"

                val message = """
                    Привет! Пройди опрос по уроку по этой ссылке:
                    $deepLink
                    
                    Если приложение еще не установлено, сначала скачай его здесь:
                    $downloadLink
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Отправить тест ученику:"))
            }
        }

        // 3. Логика переключения темы
        btnTheme.setOnClickListener {
            val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}