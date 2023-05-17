package com.chocolatecake.marvel.ui.creator_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chocolatecake.marvel.data.remote.model.ComicDto
import com.chocolatecake.marvel.data.remote.model.ProfileDto
import com.chocolatecake.marvel.data.remote.model.SeriesDto
import com.chocolatecake.marvel.data.repository.MarvelRepository
import com.chocolatecake.marvel.data.util.Status
import com.chocolatecake.marvel.ui.base.BaseViewModel
import com.chocolatecake.marvel.ui.creator_details.adapter.CreatorDetailsListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class CreatorDetailsViewModel @Inject constructor(
    private val repository: MarvelRepository
) : BaseViewModel(), CreatorDetailsListener {

    var creatorId by Delegates.notNull<Int>()

    private val _comicsList = MutableLiveData<Status<List<ComicDto>?>>()
    val comicsList: LiveData<Status<List<ComicDto>?>>
        get() = _comicsList

    private val _seriesList = MutableLiveData<Status<List<SeriesDto>?>>()
    val seriesList: LiveData<Status<List<SeriesDto>?>>
        get() = _seriesList

    private val _creator = MutableLiveData<Status<List<ProfileDto>?>>()
    val creator: LiveData<Status<List<ProfileDto>?>>
        get() = _creator


    fun loadData() {
        getCreator()
        getSeries()
        getComics()
    }

    //region Creator
    private fun getCreator() {
        _creator.postValue(Status.Loading)
        disposeResponse(
            response = repository.getCreatorById(creatorId),
            onSuccess = ::onGetCreatorSuccess,
            onFailure = ::onFailure,
        )
    }

    private fun onGetCreatorSuccess(status: Status<List<ProfileDto>>) {
        status.toData()?.let { _creator.postValue(Status.Success(it)) }
    }
    //endregion

    //region Series
    private fun getSeries() {
        _seriesList.postValue(Status.Loading)
        disposeResponse(
            response = repository.getSeriesForCreator(creatorId),
            onSuccess = ::onGetSeriesSuccess,
            onFailure = ::onFailure,
        )
    }

    private fun onGetSeriesSuccess(status: Status<List<SeriesDto>>) {
        status.toData()?.let { _seriesList.postValue(Status.Success(it)) }
    }
    //endregion

    //region Comics
    private fun getComics() {
        _comicsList.postValue(Status.Loading)
        disposeResponse(
            response = repository.getComicsForCreator(creatorId),
            onSuccess = ::onGetComicsSuccess,
            onFailure = ::onFailure,
        )
    }

    private fun onGetComicsSuccess(status: Status<List<ComicDto>>) {
        status.toData()?.let { _comicsList.postValue(Status.Success(it)) }
    }
    //endregion

    private fun onFailure(throwable: Throwable) {
        _creator.postValue(Status.Failure(throwable.message.toString()))
        _seriesList.postValue(Status.Failure(throwable.message.toString()))
        _comicsList.postValue(Status.Failure(throwable.message.toString()))
    }


    override fun onClickSeries(id: Int) {
        navigate(CreatorDetailsFragmentDirections.actionCreatorDetailsFragmentToSeriesDetailsFragment(id))
    }

    override fun onClickComic(id: Int) {
        navigate(CreatorDetailsFragmentDirections.actionCreatorDetailsFragmentToComicsDetailsFragment(id))
    }
}