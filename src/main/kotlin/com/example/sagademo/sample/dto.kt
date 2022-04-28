package com.example.sagademo.sample


data class User(val id: Int, val name: String)
data class CheckedUser(val user: User, val checkStatus: Int)