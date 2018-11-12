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
package com.josedlpozo.galileo.activities.algebras

import arrow.Kind
import arrow.instances.either.applicativeError.raiseError
import arrow.typeclasses.Monad
import arrow.typeclasses.MonadError
import com.josedlpozo.galileo.activities.model.ActivityEvent

internal interface ActivityEventDataSource<F> {
    fun add(activityEvent: ActivityEvent): Kind<F, ActivityEvent>
    fun all(): Kind<F, List<ActivityEvent>>
    fun get(id: Long): Kind<F, ActivityEvent>
}

internal interface ActivityEventUseCase<F> {
    fun get(): Kind<F, List<ActivityEvent>>
    fun get(id: Long): Kind<F, ActivityEvent>
}

internal class DefaultActivityEventDataSource<F>(private val monad: MonadError<F, Throwable>) : ActivityEventDataSource<F>,
        MonadError<F, Throwable> by monad {

    private val activityEvents: MutableList<ActivityEvent> = mutableListOf()

    override fun add(activityEvent: ActivityEvent): Kind<F, ActivityEvent> {
        if (activityEvent.activityName.contains("com.josedlpozo.galileo")) return just(activityEvent)
        activityEvents.add(activityEvent)
        return just(activityEvent)
    }

    override fun all(): Kind<F, List<ActivityEvent>> = just(activityEvents)

    override fun get(id: Long): Kind<F, ActivityEvent> = activityEvents.find { it.id == id }?.let {
        just(it)
    } ?: raiseError(NoSuchElementException())

}

internal class DefaultActivityEventUseCase<F>(private val dataSource: ActivityEventDataSource<F>) : ActivityEventUseCase<F> {

    override fun get(): Kind<F, List<ActivityEvent>> = dataSource.all()

    override fun get(id: Long): Kind<F, ActivityEvent> = dataSource.get(id)

}