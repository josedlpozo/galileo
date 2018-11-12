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

internal sealed class ActivityEvent(open val id: Long, val name: String, open val activityName: String)

internal data class Paused(override val id: Long, override val activityName: String): ActivityEvent(id, "Paused", activityName)
internal data class Resumed(override val id: Long, override val activityName: String): ActivityEvent(id, "Resumed", activityName)
internal data class Started(override val id: Long, override val activityName: String): ActivityEvent(id, "Started", activityName)
internal data class Destroyed(override val id: Long, override val activityName: String): ActivityEvent(id, "Destroyed", activityName)
internal data class SavedInstanceState(override val id: Long, override val activityName: String, val extras: List<BundleItem>): ActivityEvent(id, "SavedInstanceState", activityName)
internal data class Stopped(override val id: Long, override val activityName: String): ActivityEvent(id, "Stopped", activityName)
internal data class Created(override val id: Long, override val activityName: String, val extras: List<BundleItem>): ActivityEvent(id, "Created", activityName)