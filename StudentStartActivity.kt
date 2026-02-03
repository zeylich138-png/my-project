package com.example.teacherhelper2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class StudentStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_start)

        // ЛОГИКА ПЕРЕХВАТА ССЫЛКИ
        val data: Uri? = intent.data
        data?.getQueryParameter("teacher")?.let { teacherEmail ->
            // Автоматически сохраняем почту учителя из ссылки в память
            val prefs = getSharedPreferences("TeacherPrefs", Context.MODE_PRIVATE)
            prefs.edit().putString("saved_email", teacherEmail).apply()
            Toast.makeText(this, "Тест для учителя: $teacherEmail", Toast.LENGTH_LONG).show()
        }

        val etName = findViewById<EditText>(R.id.etStudentNameOnly)
        findViewById<Button>(R.id.btnStartQuizOnly).setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isNotEmpty()) {
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra("STUDENT_INFO", name)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Введи ФИО", Toast.LENGTH_SHORT).show()
            }
        }
    }
}