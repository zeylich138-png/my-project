package com.example.teacherhelper2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. ЗАПУСК РЕЖИМА УЧЕНИКА (локально на этом телефоне)
        findViewById<Button>(R.id.btnOpenForStudent).setOnClickListener {
            startActivity(Intent(this, StudentStartActivity::class.java))
        }

        // 2. СМЕНА ТЕМЫ
        findViewById<Button>(R.id.btnMainTheme).setOnClickListener {
            val isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        // 3. ПЕРЕХОД В НАСТРОЙКИ
        findViewById<Button>(R.id.btnGoSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}