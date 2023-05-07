package com.chocolatecake.marvel.ui.home.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chocolatecake.marvel.data.model.ComicsResult
import com.chocolatecake.marvel.data.model.EventResult
import com.chocolatecake.marvel.data.model.SeriesResult
import com.chocolatecake.marvel.data.model.base.BaseResponse
import com.chocolatecake.marvel.data.repository.MarvelRepository
import com.chocolatecake.marvel.data.repository.MarvelRepositoryImpl
import com.chocolatecake.marvel.data.util.Status
import com.chocolatecake.marvel.ui.base.BaseViewModel
import com.chocolatecake.marvel.ui.home.view.HomeListener

class HomeViewModel : BaseViewModel(), HomeListener {
    private val marvelRepository: MarvelRepository by lazy { MarvelRepositoryImpl() }

    private val _events = MutableLiveData<Status<List<EventResult>>>()
    val events: LiveData<Status<List<EventResult>>> get() = _events

    private val _series = MutableLiveData<Status<List<SeriesResult>>>()
    val series: LiveData<Status<List<SeriesResult>>> get() = _series

    private val _comics = MutableLiveData<Status<List<ComicsResult>>>()
    val comics: LiveData<Status<List<ComicsResult>>> get() = _comics

    private val _eventId = MutableLiveData<Int?>()
    val eventId: LiveData<Int?> get() = _eventId

    private val _seriesId = MutableLiveData<Int?>()
    val seriesId: LiveData<Int?> get() = _seriesId

    private val _comicId = MutableLiveData<Int?>()
    val comicId : LiveData<Int?> get()= _comicId

    private val _navigateToSeries = MutableLiveData(false)
    val navigateToSeries: LiveData<Boolean> get() = _navigateToSeries

    private val _navigateToComic = MutableLiveData(false)
    val navigateToComic : LiveData<Boolean> get() = _navigateToComic

    init {
        loadData()
    }

    fun loadData() {
        getCurrentEvent()
        getCurrentSeries()
        getCurrentComic()
    }

    private fun getCurrentEvent() {
        _events.postValue(Status.Loading)
        marvelRepository.getEvents(limit = 10, offset = (0..50).random())
            .subscribe(::onEventSuccess, ::onFailure).add()
    }

    private fun onEventSuccess(result: Status<BaseResponse<EventResult>?>) {
        result.toData()?.data?.results?.let {
            _events.postValue(Status.Success(it.filterNotNull()))
        }
    }

    private fun getCurrentSeries() {
        _series.postValue(Status.Loading)
        marvelRepository.getSeries(limit = 8, offset = (0..50).random())
            .subscribe(::onSeriesSuccess, ::onFailure).add()
    }

    private fun getCurrentComic(){
        _comics.postValue(Status.Loading)
        marvelRepository.getComics(limit = 4, offset=(1..5).random())
            .subscribe(::onComicsSuccess, ::onFailure).add()
    }

    private fun onSeriesSuccess(status: Status<BaseResponse<SeriesResult>?>) {
        status.toData()?.data?.results?.let {
            _series.postValue(Status.Success(it.filterNotNull()))
        }
    }

    private fun onComicsSuccess(status: Status<BaseResponse<ComicsResult>?>){
        status.toData()?.data?.results?.let{
            _comics.postValue(Status.Success(it.filterNotNull()))
        }
    }

    private fun onFailure(throwable: Throwable) {
        _events.postValue(Status.Failure(throwable.message.toString()))
        _series.postValue(Status.Failure(throwable.message.toString()))
        _comics.postValue(Status.Failure(throwable.message.toString()))
    }

    override fun onClickBanner(eventId: Int?) {
        _eventId.postValue(eventId)
    }

    override fun onClickSeries(seriesId: Int?) {
        _seriesId.postValue(seriesId)
    }

    override fun onClickComic(comicId: Int?) {
        _comicId.postValue(comicId)
    }

    override fun onClickMoreComics() {
        _navigateToComic.postValue(true)
    }

    override fun onClickMoreSeries() {
        _navigateToSeries.postValue(true)
    }

}