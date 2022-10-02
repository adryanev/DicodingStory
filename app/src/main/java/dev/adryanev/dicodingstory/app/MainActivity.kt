package dev.adryanev.dicodingstory.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }
}