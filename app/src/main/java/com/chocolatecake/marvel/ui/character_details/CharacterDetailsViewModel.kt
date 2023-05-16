package com.chocolatecake.marvel.ui.character_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chocolatecake.marvel.data.model.ComicsResult
import com.chocolatecake.marvel.data.model.ProfileResult
import com.chocolatecake.marvel.data.repository.MarvelRepository
import com.chocolatecake.marvel.data.repository.MarvelRepositoryImpl
import com.chocolatecake.marvel.data.util.Status
import com.chocolatecake.marvel.ui.base.BaseViewModel
import com.chocolatecake.marvel.ui.core.listener.ComicListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val repository: MarvelRepository
) : BaseViewModel(), ComicListener {

   var characterId by Delegates.notNull<Int>()

    private val _comics = MutableLiveData<Status<List<ComicsResult>>>()
    val comics: LiveData<Status<List<ComicsResult>>>
        get() = _comics

    private val _character = MutableLiveData<Status<ProfileResult>>()
    val character: LiveData<Status<ProfileResult>>
        get() = _character


    fun loadDetails() {
        loadCharacter()
        loadComics()
    }

    //region Character
    private fun loadCharacter() {
        _character.postValue(Status.Loading)
        disposeResponse(
            response= repository.getCharacterById(characterId),
            onSuccess = ::onCharacterSuccess,
            onFailure = ::onFailure,
        )
    }

    private fun onCharacterSuccess(status: Status<List<ProfileResult>>) {
        status.toData()?.first().let {
            _character.postValue(Status.Success(it!!))
        }
    }
    //endregion

    //region Comics
    private fun loadComics() {
        _comics.postValue(Status.Loading)
        disposeResponse(
            response= repository.getComicsForCharacter(characterId),
            onSuccess = ::onComicsSuccess,
            onFailure = ::onFailure,
        )
    }

    private fun onComicsSuccess(status: Status<List<ComicsResult>>) {
        status.toData()?.let {
            _comics.postValue(Status.Success(it))
        }
    }
    //endregion

    private fun onFailure(throwable: Throwable) {
        _character.postValue(Status.Failure(throwable.message.toString()))
        _comics.postValue(Status.Failure(throwable.message.toString()))
    }


    override fun onClickComic(id: Int) {
        navigate(
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToComicsDetailsFragment(
                id
            )
        )
    }
}