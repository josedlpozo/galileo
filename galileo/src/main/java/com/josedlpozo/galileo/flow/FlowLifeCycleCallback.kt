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
package com.josedlpozo.galileo.flow

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.josedlpozo.galileo.flow.algebras.FlowEventDataSource
import com.josedlpozo.galileo.flow.model.Created
import com.josedlpozo.galileo.flow.model.Destroyed
import com.josedlpozo.galileo.flow.model.Resumed
import com.josedlpozo.galileo.flow.model.items

internal class FlowLifeCycleCallback<F>(private val dataSource: FlowEventDataSource<F>) : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        val created = System.currentTimeMillis()
        dataSource.add(Resumed(created, activity.localClassName, created))
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        val created = System.currentTimeMillis()
        dataSource.add(Destroyed(created, activity.localClassName, created))
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        val created = System.currentTimeMillis()
        dataSource.add(Created(System.nanoTime(), activity.localClassName, created,
                bundle.items + activity.intent.extras.items))
    }
}