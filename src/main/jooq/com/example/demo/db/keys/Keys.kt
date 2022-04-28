/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.keys


import com.example.demo.db.tables.Saga
import com.example.demo.db.tables.SagaStep
import com.example.demo.db.tables.SagaStepError
import com.example.demo.db.tables.records.SagaRecord
import com.example.demo.db.tables.records.SagaStepErrorRecord
import com.example.demo.db.tables.records.SagaStepRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val SAGA_PKEY: UniqueKey<SagaRecord> = Internal.createUniqueKey(Saga.SAGA, DSL.name("saga_pkey"), arrayOf(Saga.SAGA.ID), true)
val SAGA_STEP_PKEY: UniqueKey<SagaStepRecord> = Internal.createUniqueKey(SagaStep.SAGA_STEP, DSL.name("saga_step_pkey"), arrayOf(SagaStep.SAGA_STEP.ID), true)
val SAGA_STEP_ERROR_PKEY: UniqueKey<SagaStepErrorRecord> = Internal.createUniqueKey(SagaStepError.SAGA_STEP_ERROR, DSL.name("saga_step_error_pkey"), arrayOf(SagaStepError.SAGA_STEP_ERROR.ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val SAGA_STEP__SAGA_STEP_SAGA_ID_FKEY: ForeignKey<SagaStepRecord, SagaRecord> = Internal.createForeignKey(SagaStep.SAGA_STEP, DSL.name("saga_step_saga_id_fkey"), arrayOf(SagaStep.SAGA_STEP.SAGA_ID), com.example.demo.db.keys.SAGA_PKEY, arrayOf(Saga.SAGA.ID), true)
val SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_ID_FKEY: ForeignKey<SagaStepErrorRecord, SagaRecord> = Internal.createForeignKey(SagaStepError.SAGA_STEP_ERROR, DSL.name("saga_step_error_saga_id_fkey"), arrayOf(SagaStepError.SAGA_STEP_ERROR.SAGA_ID), com.example.demo.db.keys.SAGA_PKEY, arrayOf(Saga.SAGA.ID), true)
val SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_STEP_ID_FKEY: ForeignKey<SagaStepErrorRecord, SagaStepRecord> = Internal.createForeignKey(SagaStepError.SAGA_STEP_ERROR, DSL.name("saga_step_error_saga_step_id_fkey"), arrayOf(SagaStepError.SAGA_STEP_ERROR.SAGA_STEP_ID), com.example.demo.db.keys.SAGA_STEP_PKEY, arrayOf(SagaStep.SAGA_STEP.ID), true)