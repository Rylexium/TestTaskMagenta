package com.example.test_task_magents.activity.picture.fragment.favorite

import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.util.workWith.getAllFavoritePicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FavoritePictureViewModel : ViewModel() {
    private var favoritePictureList : MutableLiveData<MutableList<FavoritePicture>> = MutableLiveData()
    private var state: Parcelable? = null

    fun setScrollState(state: Parcelable?) {
        this.state = state
    }

    fun getScrollState(): Parcelable? {
        return state
    }

    fun getFavoritePictureList(): MutableLiveData<MutableList<FavoritePicture>> {
        return favoritePictureList
    }

    suspend fun selectFavoritePicturesFromDB(): MutableList<FavoritePicture>? {
        return suspendCoroutine {
            var data : List<FavoritePicture>?
            CoroutineScope(Dispatchers.IO).launch {
                data = getAllFavoritePicture()
                val selectedListFromDB = ArrayList<FavoritePicture>()

                if(data != null) {
                    for(picture in data!!)
                        selectedListFromDB.add(FavoritePicture(picture.id, picture.author, picture.picture))
                    Handler(Looper.getMainLooper()).post {
                        favoritePictureList.value = selectedListFromDB
                    }
                    it.resume(favoritePictureList.value)
                }
                else it.resume(null)
            }
        }
    }
}