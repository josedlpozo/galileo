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
import arrow.typeclasses.Monad
import com.josedlpozo.galileo.activities.model.Event

internal interface ActivityLifeCycleDataSource<F> {
    fun add(event: Event): Kind<F, Event>
    fun all(): Kind<F, List<Event>>
}

internal interface ActivityLifeCycleUseCase<F> {
    fun get(): Kind<F, List<Event>>
}

internal class DefaultActivityLifeCycleDataSource<F>(private val monad: Monad<F>) : ActivityLifeCycleDataSource<F>,
        Monad<F> by monad {

    private val events: MutableList<Event> = mutableListOf()

    override fun add(event: Event): Kind<F, Event> {
        if (event.activityName.contains("com.josedlpozo.galileo")) return just(event)
        events.add(event)
        return just(event)
    }

    override fun all(): Kind<F, List<Event>> = just(events)

}

internal class DefaultActivityLifeCycleUseCase<F>(private val dataSource: ActivityLifeCycleDataSource<F>) : ActivityLifeCycleUseCase<F> {

    override fun get(): Kind<F, List<Event>> = dataSource.all()

}