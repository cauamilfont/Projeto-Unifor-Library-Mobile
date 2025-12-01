package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModerationActivity : BaseActivity() {
    override fun getBottomNavItemId() = R.id.navigation_admin_moderacao;

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModerationAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_moderacao)



        setupBottomNavigation()

        recyclerView = findViewById(R.id.recyclerViewComentarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ModerationAdapter(mutableListOf()) { id, approved ->
            handleModeration(id, approved)
        }


        recyclerView.adapter = adapter
        loadPendingReviews()
    }

    override fun onStart() {
        super.onStart()

}

    private fun loadPendingReviews() {
        lifecycleScope.launch(Dispatchers.IO) {
            val reviews = DatabaseHelper.getPendingReviews()


            val uiList = reviews.map {
                Comment(it.id,it.userId, 0,  it.bookTitle, it.commentContent, it.ratingContent, it.ratingPhysical, it.authorName)
            }.toMutableList()

            withContext(Dispatchers.Main) {
                adapter.updateList(uiList)
            }
        }
    }

    private fun handleModeration(idString: String, approved: Boolean) {
        val id = idString.toIntOrNull() ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val success = DatabaseHelper.moderateReview(id, approved)

            withContext(Dispatchers.Main) {
                if (success) {
                    val msg = if (approved) "Avaliação Aprovada" else "Avaliação Rejeitada"
                    Toast.makeText(this@ModerationActivity, msg, Toast.LENGTH_SHORT).show()
                    // O adapter já removeu o item visualmente, mas é bom garantir sincronia
                } else {
                    Toast.makeText(this@ModerationActivity, "Erro ao processar.", Toast.LENGTH_SHORT).show()
                    loadPendingReviews()
                }
            }
        }
    }
    }