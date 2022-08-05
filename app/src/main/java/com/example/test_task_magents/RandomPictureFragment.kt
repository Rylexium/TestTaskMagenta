package com.example.test_task_magents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task_magents.adapter.PictureAdapter
import com.example.test_task_magents.databinding.RandomPictureFragmentBinding
import com.example.test_task_magents.model.PictureData
import kotlinx.coroutines.*
import kotlin.random.Random


class RandomPictureFragment : Fragment() {
    private var _binding: RandomPictureFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recv : RecyclerView
    private var pictureList : ArrayList<PictureData> = ArrayList()
    private lateinit var pictureAdapter: PictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomPictureFragmentBinding.inflate(inflater, container, false)
        Toast.makeText(context, "Random", Toast.LENGTH_SHORT).show()


        recv = binding.randomPictureRecyclerView
        pictureAdapter = PictureAdapter(this, pictureList)

        recv.layoutManager = LinearLayoutManager(activity)
        recv.adapter = pictureAdapter

        CoroutineScope(Dispatchers.Unconfined).launch{
            for (i in 0..1000) {
                onAddField()
            }
        }

        return binding.root
    }



    private suspend fun onAddField() {
        return coroutineScope {
            pictureList.add(PictureData("Daniel Ebersole", "117", "https://picsum.photos/200/300?random=" + Random.nextInt(), false))
            pictureAdapter.notifyDataSetChanged()
        }
    }
}