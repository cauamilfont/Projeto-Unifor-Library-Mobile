
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import android.widget.PopupMenu
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ExploreBooksActivity : BaseActivity() {

    override fun getBottomNavItemId() = R.id.navigation_inicio;

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : ExploreBooksAdapter
    lateinit var chatButton : FloatingActionButton

    lateinit var etSearch: EditText
    lateinit var btnFilter: ImageView

    private var allBooksList: List<Book> = emptyList()
    private var filteredList: List<Book> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorar_livros)

        chatButton = findViewById(R.id.fab_chat)
        recyclerView = findViewById(R.id.recyclerViewBooks)
        etSearch = findViewById(R.id.etBuscarLivros)
        btnFilter = findViewById(R.id.ivFiltro)

        adapter = ExploreBooksAdapter(emptyList())
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter


        loadBooks()


        setupBottomNavigation()


        setupSearch()


        setupFilterMenu()
    }

    private fun loadBooks() {
        lifecycleScope.launch(Dispatchers.IO) {

            allBooksList = DatabaseHelper.getAllBooks()


            filteredList = ArrayList(allBooksList)

            withContext(Dispatchers.Main) {

                adapter.updateList(filteredList)
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterBooks(s.toString())
            }
        })
    }

    private fun filterBooks(query: String) {
        val searchText = query.lowercase(Locale.getDefault())


        filteredList = if (searchText.isEmpty()) {
            ArrayList(allBooksList)
        } else {
            allBooksList.filter { book ->

                book.title.lowercase(Locale.getDefault()).contains(searchText) ||
                        book.author.lowercase(Locale.getDefault()).contains(searchText)
            }
        }

        adapter.updateList(filteredList)
    }

    private fun setupFilterMenu() {
        btnFilter.setOnClickListener { view ->
            val popup = PopupMenu(this, view)

            popup.menuInflater.inflate(R.menu.menu_filter_books, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_sort_name_asc -> sortBooks { it.title }
                    R.id.action_sort_name_desc -> sortBooks(descending = true) { it.title }


                    R.id.action_sort_quality_asc -> sortBooks { it.bookQuality ?: 0f }
                    R.id.action_sort_quality_desc -> sortBooks(descending = true) { it.bookQuality ?: 0f }


                    R.id.action_sort_physical_asc -> sortBooks { it.physicalQuality ?: 0f }
                    R.id.action_sort_physical_desc -> sortBooks(descending = true) { it.physicalQuality ?: 0f }
                }
                true
            }
            popup.show()
        }
    }

    private fun <T : Comparable<T>> sortBooks(descending: Boolean = false, selector: (Book) -> T) {
        filteredList = if (descending) {
            filteredList.sortedByDescending(selector)
        } else {
            filteredList.sortedBy(selector)
        }
        adapter.updateList(filteredList)
    }

    override fun onStart() {
        super.onStart()
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }

    }

}