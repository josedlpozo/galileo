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
package com.josedlpozo.galileo.activities

import arrow.core.ForId
import arrow.core.Id
import arrow.instances.id.monad.monad
import com.josedlpozo.galileo.activities.algebras.ActivityLifeCycleUseCase
import com.josedlpozo.galileo.activities.algebras.DefaultActivityLifeCycleDataSource
import com.josedlpozo.galileo.activities.algebras.DefaultActivityLifeCycleUseCase

internal interface GalileoActivityLifeCycleInstances<F> {

    val dataSource: DefaultActivityLifeCycleDataSource<F>

    val galileoActivityLifeCycleCallback: GalileoActivityLifeCycleCallback<F>

    val useCase: ActivityLifeCycleUseCase<F>
}

internal object GalileoActivityLifeCycleID : GalileoActivityLifeCycleInstances<ForId> {

    override val dataSource = DefaultActivityLifeCycleDataSource(Id.monad())

    override val galileoActivityLifeCycleCallback = GalileoActivityLifeCycleCallback(dataSource)

    override val useCase = DefaultActivityLifeCycleUseCase(dataSource)

}