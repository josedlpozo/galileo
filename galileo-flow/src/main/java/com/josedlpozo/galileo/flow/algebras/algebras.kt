/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.flow.algebras

import arrow.Kind
import arrow.typeclasses.MonadError
import com.josedlpozo.galileo.flow.model.FlowEvent

internal interface FlowEventDataSource<F> {
    fun add(flowEvent: FlowEvent): Kind<F, FlowEvent>
    fun all(): Kind<F, List<FlowEvent>>
    fun get(id: Long): Kind<F, FlowEvent>
}

internal interface FlowEventUseCase<F> {
    fun get(): Kind<F, List<FlowEvent>>
    fun get(id: Long): Kind<F, FlowEvent>
}

internal class DefaultFlowEventDataSource<F>(private val monad: MonadError<F, Throwable>) : FlowEventDataSource<F>,
                                                                                            MonadError<F, Throwable> by monad {

    companion object {
        private const val MAX_SIZE = 300
    }

    private val flowEvents: MutableList<FlowEvent> = mutableListOf()

    override fun add(flowEvent: FlowEvent): Kind<F, FlowEvent> {
        if (flowEvent.activityName.contains("com.josedlpozo.galileo")) return just(flowEvent)
        flowEvents.add(flowEvent)
        if (flowEvents.size > MAX_SIZE) {
            flowEvents.drop(flowEvents.size - MAX_SIZE)
        }
        return just(flowEvent)
    }

    override fun all(): Kind<F, List<FlowEvent>> = just(flowEvents)

    override fun get(id: Long): Kind<F, FlowEvent> = flowEvents.find { it.id == id }?.let {
        just(it)
    } ?: raiseError(NoSuchElementException())

}

internal class DefaultFlowEventUseCase<F>(private val dataSource: FlowEventDataSource<F>) : FlowEventUseCase<F> {

    override fun get(): Kind<F, List<FlowEvent>> = dataSource.all()

    override fun get(id: Long): Kind<F, FlowEvent> = dataSource.get(id)

}