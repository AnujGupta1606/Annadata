package com.anuj.feedforward

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatBotActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var progressBar: ProgressBar

    // Explicitly typed RecyclerView components to solve the 7 problems
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val chatList: MutableList<ChatMessage> = mutableListOf<ChatMessage>()

    // Corrected model name to 1.5-flash (2.5 doesn't exist yet!)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = "AIzaSyAKmlqzCpjyTkEWhicXdFi8iJMGUF6Ywok"
    )
    // send both to me

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        // Initialize UI Elements
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        progressBar = findViewById(R.id.pbLoading)
        val closeChat = findViewById<ImageView>(R.id.closeChat)

        // Setup RecyclerView with explicit types
        chatRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        chatAdapter = ChatAdapter(chatList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        closeChat.setOnClickListener { finish() }

        btnSend.setOnClickListener {
            val query = etMessage.text.toString().trim()
            if (query.isNotEmpty()) {
                addUserMessage(query)
                askGemini(query)
                etMessage.text.clear()
            }
        }
    }

    private fun addUserMessage(message: String) {
        chatList.add(ChatMessage(message, true))
        chatAdapter.notifyItemInserted(chatList.size - 1)
        chatRecyclerView.scrollToPosition(chatList.size - 1)
    }

    private fun askGemini(query: String) {
        progressBar.visibility = View.VISIBLE
        btnSend.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(query)
                }

                val aiText = response.text ?: "I'm not sure about that."

                // Add AI response to the list so it stays on screen
                chatList.add(ChatMessage(aiText, false))
                chatAdapter.notifyItemInserted(chatList.size - 1)

                // Smooth scroll to the new message
                chatRecyclerView.scrollToPosition(chatList.size - 1)

            } catch (e: Exception) {
                Log.e("GEMINI_ERROR", "Reason: ${e.localizedMessage}")
                Toast.makeText(this@ChatBotActivity, "Check connection: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSend.isEnabled = true
            }
        }
    }
}
