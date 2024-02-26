/*
Allows for user to input name and keep it
@author cdavignon
@date 2/25/24
 */
package edu.quinnipiac.ser210.assignment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class View : ViewModel() {
    private val name = MutableLiveData<String>()
    val text: LiveData<String> = name

    fun setText(input: String) {
        name.value = input
    }
}