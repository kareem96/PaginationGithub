package com.kareem.appusergithub.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kareem.appusergithub.data.model.UserItems
import com.kareem.appusergithub.presentation.repository.Repository
import com.kareem.appusergithub.presentation.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.mockito.MockitoAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*


class MainViewModelTest {

    private val name = "S"

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    var repository: Repository? = null
    private var mainViewModel: MainViewModel? = null

    private val _searchUser = MutableLiveData<ArrayList<UserItems>>()
    val searchUser:LiveData<ArrayList<UserItems>> = _searchUser


    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        mainViewModel = repository?.let {
            mainViewModel
        }
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun searchUser(){
        runBlocking {
            Mockito.`when`(repository?.searchUser).thenReturn(searchUser)
            mainViewModel?.getSearch(name)
            val data = mainViewModel?.getSearch(name)?.value
            Assert.assertNotNull(data)

        }
    }
}

