/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.sequences


import com.example.demo.db.StateService

import org.jooq.Sequence
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType



/**
 * The sequence <code>state_service.ship_order_id_seq</code>
 */
val SHIP_ORDER_ID_SEQ: Sequence<Int> = Internal.createSequence("ship_order_id_seq", StateService.STATE_SERVICE, SQLDataType.INTEGER.nullable(false), null, null, null, null, false, null)