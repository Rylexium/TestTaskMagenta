package com.example.test_task_magents.db.repository

import com.example.test_task_magents.db.dao.FavoritePictureDao
import com.example.test_task_magents.db.model.FavoritePicture

class FavoritePictureRealization(private val favoritePictureDao : FavoritePictureDao) : FavoritePictureRepository {
    override val allFavoritePicture: List<FavoritePicture>
        get() = favoritePictureDao.getAll()

    override suspend fun insertFavoritePicture(
        favoritePicture: FavoritePicture,
    ) {
        favoritePictureDao.insert(favoritePicture)
    }

    override suspend fun deleteFavoritePicture(favoritePicture: FavoritePicture) {
        favoritePictureDao.delete(favoritePicture)
    }
}