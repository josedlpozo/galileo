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

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.josedlpozo.galileo.activities.algebras.ActivityEventDataSource
import com.josedlpozo.galileo.activities.model.*

internal class GalileoActivityLifeCycleCallback<F>(private val dataSource: ActivityEventDataSource<F>) : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
        dataSource.add(Paused(activity.localClassName))
    }

    override fun onActivityResumed(activity: Activity) {
        dataSource.add(Resumed(activity.localClassName))
    }

    override fun onActivityStarted(activity: Activity) {
        dataSource.add(Started(activity.localClassName))
    }

    override fun onActivityDestroyed(activity: Activity) {
        dataSource.add(Destroyed(activity.localClassName))
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
        dataSource.add(SavedInstanceState(activity.localClassName, bundle.items))
    }

    override fun onActivityStopped(activity: Activity) {
        dataSource.add(Stopped(activity.localClassName))
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        dataSource.add(Created(activity.localClassName, bundle.items, activity.intent.extras.items))
    }
}