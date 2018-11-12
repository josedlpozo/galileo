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
package com.josedlpozo.galileo.activities.model

internal sealed class ActivityEvent(val name: String, open val activityName: String)

internal data class Paused(override val activityName: String): ActivityEvent("Paused", activityName)
internal data class Resumed(override val activityName: String): ActivityEvent("Resumed", activityName)
internal data class Started(override val activityName: String): ActivityEvent("Started", activityName)
internal data class Destroyed(override val activityName: String): ActivityEvent("Destroyed", activityName)
internal data class SavedInstanceState(override val activityName: String, val bundleValues: List<BundleItem>): ActivityEvent("SavedInstanceState", activityName)
internal data class Stopped(override val activityName: String): ActivityEvent("Stopped", activityName)
internal data class Created(override val activityName: String, val bundleValues: List<BundleItem>, val extras: List<BundleItem>): ActivityEvent("Created", activityName)