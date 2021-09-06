package com.example.fantapp.model

import kotlin.collections.ArrayList
import com.example.fantapp.UserObserver

class User private constructor() {
    private var token: String = ""
    private var username: String = ""
    private var password: String = ""

    private fun notifyObservers() {
        observers.forEach( fun (observer){
            observer.userUpdate()
        })
    }

    companion object {
        private var user: User? = null
        private var observers: ArrayList<UserObserver> = ArrayList()

        fun getInstance(): User {
            if (user == null) {
                user = User()
            }
            return user!!
        }

        fun observe(observer: UserObserver) {
            observers.add(observer)
        }
    }

    public fun setToken(token: String?) {
        if (token == null) return

        this.token = token
        notifyObservers()
    }

    public fun getToken(): String{
        return this.token
    }

    public fun setUsername(username: String?) {
        if (username == null) return

        this.username = username
        notifyObservers()
    }

    public fun getUsername(): String{
        return this.username
    }

    public fun setPassword(password: String?) {
        if (password == null) return
        this.password = password
        notifyObservers()
    }

    public fun getPassword(): String{
        return this.password
    }
}