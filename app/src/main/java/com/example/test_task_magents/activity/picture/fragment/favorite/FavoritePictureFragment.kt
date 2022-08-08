package com.example.test_task_magents.activity.picture.fragment.favorite

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.test_task_magents.adapter.FavoritePictureAdapter
import com.example.test_task_magents.databinding.FavoritePictureFragmentBinding
import kotlinx.coroutines.*

class FavoritePictureFragment : Fragment() {
    private var _binding: FavoritePictureFragmentBinding? = null

    private val binding get() = _binding!!
    private lateinit var recv : RecyclerView
    private lateinit var favoritePictureAdapter: FavoritePictureAdapter
    private lateinit var viewModel: FavoritePictureViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritePictureFragmentBinding.inflate(inflater, container, false)

        initComponents()
        showFavoritePictures()
        return binding.root
    }

    private fun initComponents() {
        viewModel = ViewModelProvider(this)[FavoritePictureViewModel::class.java]
        recv = binding.favoritePictureRecyclerView
        swipeRefreshLayout = binding.swipeRefreshLayout
        viewModel.getFavoritePictureList().observe(viewLifecycleOwner) {
            favoritePictureAdapter = FavoritePictureAdapter(this, it)
            recv.adapter = favoritePictureAdapter
            recv.layoutManager = LinearLayoutManager(activity)
            (recv.layoutManager as LinearLayoutManager) //скролим до нужного момента
                .onRestoreInstanceState(viewModel.getScrollState())
        }

        swipeRefreshLayout.setOnRefreshListener {
            showFavoritePictures()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setScrollState(recv.layoutManager?.onSaveInstanceState())
    }


    private fun showFavoritePictures() {
        binding.progressDownloadPicture.visibility = View.VISIBLE
        viewModel.viewModelScope.launch {
            viewModel.selectFavoritePicturesFromDB()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressDownloadPicture.visibility = View.GONE
            }, System.currentTimeMillis() % 250)
        }
    }

}