package com.example.test_task_magents.activity.picture.fragment.random

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task_magents.adapter.RandomPictureAdapter
import com.example.test_task_magents.util.workWith.getAllFavoritePicture
import com.example.test_task_magents.databinding.RandomPictureFragmentBinding
import com.example.test_task_magents.util.AnimView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class RandomPictureFragment : Fragment() {
    private var _binding: RandomPictureFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recv : RecyclerView
    companion object {
        lateinit var randomPictureAdapter: RandomPictureAdapter //static для обновления status favorite после удаления
    }
    private lateinit var snackbar: Snackbar
    private lateinit var viewModel : RandomPictureViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomPictureFragmentBinding.inflate(inflater, container, false)

        initComponents()
        applyEvents()
        firstDownloadPicture()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(recv.layoutManager != null)
            (recv.layoutManager as LinearLayoutManager) //скролим до нужного момента
                .onRestoreInstanceState(viewModel.getScrollState())
        CoroutineScope(Dispatchers.IO).launch {
            getAllFavoritePicture()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setScrollState(recv.layoutManager?.onSaveInstanceState())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setScrollState(recv.layoutManager?.onSaveInstanceState())
    }

    private fun initComponents() {
        viewModel = ViewModelProvider(this)[RandomPictureViewModel::class.java]
        recv = binding.randomPictureRecyclerView

        viewModel.getLiveDataPictureList().observe(viewLifecycleOwner) {
            randomPictureAdapter = RandomPictureAdapter(this, it)
            recv.adapter = randomPictureAdapter
            recv.layoutManager = LinearLayoutManager(activity)
            (recv.layoutManager as LinearLayoutManager) //скролим до нужного момента
                .onRestoreInstanceState(viewModel.getScrollState())
        }

        snackbar = Snackbar.make(recv, "Картинки закончились(((", Snackbar.LENGTH_LONG)
            .setAction("Любимые картинки") {
                Toast.makeText(context, "Свайпни вправо))", Toast.LENGTH_SHORT).show()
            }
            .setTextColor(Color.parseColor("#fdfffe"))
            .setBackgroundTint(Color.parseColor("#555555"))
            .setActionTextColor(Color.parseColor("#fdfffe"))
    }

    private fun applyEvents() {
        recv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    showPictures()
                }
            }
        })
    }

    private fun firstDownloadPicture() {
        if(viewModel.getLiveDataPictureList().value == null) //впервые открыли -> качаем картинки
            showPictures()
        else
            AnimView.animGone(binding.progressDownloadPicture, 100) //уже есть картинки -> убрать прогресс бар
    }

    private fun showPictures() {
        AnimView.animVisible(binding.progressDownloadPicture, 100)
        viewModel.setScrollState(recv.layoutManager?.onSaveInstanceState())
        viewModel.viewModelScope.launch {
            val pictures = viewModel.downloadPictures()

            if (pictures == null && !snackbar.isShown)
                snackbar.show()
            else if (pictures == false)
                Toast.makeText(context, "Что-то пошло не так при загрузке", Toast.LENGTH_SHORT)
                    .show()

            randomPictureAdapter.notifyItemRangeInserted(
                    (10 - viewModel.getListPagesSize()) * viewModel.getLimit(),
                viewModel.getLimit())

            Handler(Looper.getMainLooper()).postDelayed({
                AnimView.animGone(binding.progressDownloadPicture, 100)
                (recv.layoutManager as LinearLayoutManager) //скролим до нужного момента
                    .onRestoreInstanceState(viewModel.getScrollState())
            }, System.currentTimeMillis() % 250)
        }
    }
}