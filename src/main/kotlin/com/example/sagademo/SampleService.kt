package com.example.sagademo

import org.springframework.stereotype.Service

@Service
class SampleService {
    fun createUser(user: User): User {
        println("create")
        return user
    }

    fun removeUser(user: User): User {
        println("remove")
        return user
    }

    fun checkUser(user: User): CheckedUser {
        println("check")
        return CheckedUser(user, 1)
    }

    fun approveUser(user: CheckedUser): String {
        println("approve")
        return "qq"
    }

    fun notifyUser(email: String) {
        println("notify")
    }
}

data class User(val id: Int, val name: String)
data class CheckedUser(val user: User, val checkStatus: Int)