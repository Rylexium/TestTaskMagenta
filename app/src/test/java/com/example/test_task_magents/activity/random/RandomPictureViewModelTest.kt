package com.example.test_task_magents.activity.random
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.test_task_magents.activity.picture.fragment.random.RandomPictureViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RandomPictureViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RandomPictureViewModel


    @Before
    fun init() {
        viewModel = RandomPictureViewModel()
    }

    @Test
    fun `test get correct limit`() {
        assertEquals(100, viewModel.getLimit())
    }


    @Test
    fun `test remove number page after download`() = runBlocking {
        val beforeDownload = viewModel.getListPagesSize()
        viewModel.downloadPictures()
        val afterDownload = viewModel.getListPagesSize()
        assertEquals(1, beforeDownload - afterDownload)
    }

    @Test
    fun `test download pictures`() = runBlocking {
        val pictures = viewModel.downloadPictures()
        assertNotEquals(null, pictures)
    }

    @Test
    fun `try download pictures after 10 download`() = runBlocking {
        for(i in 0..viewModel.getListPagesSize())
            viewModel.downloadPictures()

        assertEquals(null, viewModel.downloadPictures())
        assertEquals(0, viewModel.getListPagesSize())
    }

}