package com.example.test_task_magents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.test_task_magents.databinding.FavoritePictureFragmentBinding

class FavoritePictureFragment : Fragment() {
    private var _binding: FavoritePictureFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritePictureFragmentBinding.inflate(inflater, container, false)
        Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show()
        return binding.root
    }
}