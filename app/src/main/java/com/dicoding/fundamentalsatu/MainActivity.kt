package com.dicoding.fundamentalsatu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.fundamentalsatu.data.model.ResponseUserGithub
import com.dicoding.fundamentalsatu.databinding.ActivityMainBinding
import com.dicoding.fundamentalsatu.detail.DetailActivity
import com.dicoding.fundamentalsatu.utils.Result

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter{ user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username",user.login)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.getUser(p0.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false

        })

        viewModel.resultUser.observe(this){
            when(it){
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUserGithub.Item>)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()

                }
                is Result.Loading ->{
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getUser()

    }
}
