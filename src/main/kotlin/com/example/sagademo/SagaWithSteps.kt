package com.example.sagademo

import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep

data class SagaWithSteps(
    val saga: Saga,
    val steps: List<SagaStep>,
){
    fun getStep(stepNumber: Int) = stepByNumber[stepNumber]
    private val stepByNumber = steps.associateBy { it.stepNumber }
}