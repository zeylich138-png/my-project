package com.example.teacherhelper2

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import kotlin.concurrent.thread

class QuizActivity : AppCompatActivity() {
    private val SENDER_EMAIL = "teacherhelper700@gmail.com"
    private val SENDER_PASSWORD = "lwadyqfbwzymrymv"

    private val questions = listOf(
        "Сложные слова на уроке объясняются простым языком.", "Когда учитель приводит примеры, тема сразу становится понятнее.",
        "Я легко могу пересказать своими словами то, что мы только что разобрали.", "После урока у меня в голове есть четкий план: что именно я сегодня узнал.",
        "Инструкции к заданиям мне ясны с первого раза.", "Мне хватает времени, чтобы довести дело до конца.",
        "Если я застрял на задаче, я знаю, где подсмотреть подсказку.", "Я могу выполнить домашнее задание сам (без интернета/родителей).",
        "В классе никто не смеется над чужими ошибками.", "Если я признаюсь, что запутался, мне спокойно помогут.",
        "Учитель замечает не только ошибки, но и мои маленькие успехи.", "На уроке я чувствую себя уверенно, а не напряженно.",
        "На уроке мы обсуждаем вещи, которые пригодятся в жизни.", "Время на уроке пролетает быстро.",
        "Задания бывают необычными, а не только по учебнику.", "У меня есть возможность высказать свое мнение.",
        "Когда мне ставят оценку, я точно знаю, за что её получил.", "Ошибки в тетради для меня — это подсказка, а не «приговор».",
        "В конце урока мы обсуждаем, что было самым трудным.", "Я чувствую, что с каждым уроком у меня получается лучше."
    )

    private var currentIdx = 0
    private val answers = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val buttons = listOf<Button>(
            findViewById(R.id.btn1), findViewById(R.id.btn2),
            findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5)
        )

        buttons.forEachIndexed { i, btn ->
            btn.setOnClickListener {
                answers[currentIdx] = i + 1
                if (currentIdx < questions.size - 1) {
                    currentIdx++
                    updateUI()
                } else {
                    sendEmail()
                }
            }
        }
        updateUI()
    }

    private fun updateUI() {
        val isDark = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        findViewById<TextView>(R.id.tvQuestion).apply {
            text = questions[currentIdx]
            setTextColor(if (isDark) Color.WHITE else Color.BLACK)
        }
        findViewById<TextView>(R.id.tvCounter).text = "Вопрос ${currentIdx + 1} из 20"
        findViewById<ProgressBar>(R.id.progressBar).progress = (currentIdx + 1) * 5
    }

    private fun sendEmail() {
        val prefs = getSharedPreferences("TeacherPrefs", Context.MODE_PRIVATE)
        val targetEmail = prefs.getString("saved_email", "teacherhelper700@gmail.com") ?: "teacherhelper700@gmail.com"
        val info = intent.getStringExtra("STUDENT_INFO") ?: "Ученик"

        findViewById<TextView>(R.id.tvQuestion).text = "⏳ Идет глубокий анализ данных..."
        findViewById<LinearLayout>(R.id.btnContainer).visibility = View.GONE

        thread {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.socketFactory.port", "465")
                    put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.port", "465")
                }
                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication() = PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD)
                })
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(SENDER_EMAIL))
                    addRecipient(Message.RecipientType.TO, InternetAddress(targetEmail))
                    subject = "Педагогический анализ: $info"
                    setText(generateDeepPedagogicalReport(info))
                }
                Transport.send(message)
                runOnUiThread { showFinishScreen() }
            } catch (e: Exception) {
                runOnUiThread {
                    findViewById<LinearLayout>(R.id.btnContainer).visibility = View.VISIBLE
                    updateUI()
                }
            }
        }
    }

    private fun generateDeepPedagogicalReport(info: String): String {
        val sb = StringBuilder()
        sb.append("ПРОФЕССИОНАЛЬНЫЙ ОТЧЕТ ДЛЯ УЧИТЕЛЯ\n")
        sb.append("Ученик: $info\n")
        sb.append("==========================================\n\n")

        val cognitive = (0..3).map { answers[it] ?: 0 }
        val safety = (8..11).map { answers[it] ?: 0 }
        val q8 = answers[7] ?: 0
        val q14 = answers[13] ?: 0

        val cognAvg = cognitive.average()
        val safeAvg = safety.average()

        // 1. КОГНИТИВНАЯ ДОСТУПНОСТЬ
        sb.append("1. ДОСТУПНОСТЬ МАТЕРИАЛА\n")
        if (cognAvg < 3.5) {
            sb.append("⚠️ ДИАГНОЗ: Ученик не понимает ваш 'профессиональный код'.\n")
            sb.append("💡 СОВЕТ: Используйте метод «EL5» (объясни так, будто мне 5 лет). Снизьте абстрактность терминов.\n\n")
        } else {
            sb.append("✅ СТАТУС: Материал усваивается хорошо.\n\n")
        }

        // 2. САМОСТОЯТЕЛЬНОСТЬ
        sb.append("2. САМОСТОЯТЕЛЬНОСТЬ (ДЕТЕКТОР ЛЖИ)\n")
        if (q8 <= 2) {
            sb.append("⚠️ ДИАГНОЗ: 'Иллюзия понимания'. Ученик кивает на уроке, но дома будет беспомощен.\n")
            sb.append("💡 СОВЕТ: На следующем уроке дайте ему решить аналогичную задачу без вашей помощи вообще.\n\n")
        } else {
            sb.append("✅ СТАТУС: Навык переходит в самостоятельное действие.\n\n")
        }

        // 3. ПСИХОЛОГИЧЕСКИЙ КЛИМАТ
        sb.append("3. ПСИХОЛОГИЧЕСКИЙ БАРЬЕР\n")
        if (safeAvg < 3.5) {
            sb.append("⚠️ ДИАГНОЗ: 'Защитная пассивность'. Ученик боится ошибки больше, чем хочет знания.\n")
            sb.append("💡 СОВЕТ: Легализуйте ошибки. Расскажите о своих промахах. Хвалите за попытку рассуждать.\n\n")
        } else {
            sb.append("✅ СТАТУС: Ученик чувствует себя в безопасности.\n\n")
        }

        // 4. ВОВЛЕЧЕННОСТЬ
        sb.append("4. ВОВЛЕЧЕННОСТЬ\n")
        if (q14 <= 3) {
            sb.append("⚠️ ДИАГНОЗ: Режим 'отбывания времени'.\n")
            sb.append("💡 СОВЕТ: Добавьте кейс из жизни. Ему нужен ответ на вопрос 'Зачем мне это?'.\n\n")
        } else {
            sb.append("✅ СТАТУС: Время на уроке имеет ценность для ученика.\n\n")
        }

        // 5. ИТОГОВЫЙ ACTION PLAN
        sb.append("==========================================\n")
        sb.append("РЕКОМЕНДАЦИЯ НА СЛЕДУЮЩИЙ УРОК:\n")
        when {
            safeAvg < 3.5 -> sb.append("- Сфокусируйтесь на эмоциональной поддержке. Хвалите за смелость в ответах.\n")
            q8 <= 2 -> sb.append("- Проверьте понимание алгоритма действий. Попросите объяснить решение вслух.\n")
            cognAvg < 3.5 -> sb.append("- Подготовьте визуальную опору (схему/карту). Избегайте длинных лекций.\n")
            else -> sb.append("- Ученик готов к усложнению. Дайте задание творческого или исследовательского уровня.\n")
        }

        sb.append("\n------------------------------------------\n")
        sb.append("Teacher Helper: Аналитическая поддержка учителя.")
        return sb.toString()
    }

    private fun showFinishScreen() {
        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        tvQuestion.text = "✅ ГОТОВО!\n\nТвои ответы отправлены учителю.\nМожешь закрывать приложение."
        tvQuestion.gravity = Gravity.CENTER
        tvQuestion.textSize = 20f
        val container = findViewById<LinearLayout>(R.id.btnContainer)
        container.removeAllViews()
        val btnClose = Button(this).apply {
            text = "ЗАКРЫТЬ"
            setBackgroundColor(Color.parseColor("#4CAF50"))
            setTextColor(Color.WHITE)
            setOnClickListener { finishAffinity() }
        }
        container.addView(btnClose)
        container.visibility = View.VISIBLE
    }
}