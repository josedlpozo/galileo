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
package com.josedlpozo.galileo.flow.model

internal sealed class FlowEvent(open val id: Long, val name: String, open val activityName: String)

internal data class Resumed(override val id: Long, override val activityName: String): FlowEvent(id, "Resumed", activityName)
internal data class Destroyed(override val id: Long, override val activityName: String): FlowEvent(id, "Destroyed", activityName)
internal data class Created(override val id: Long, override val activityName: String, val extras: List<BundleItem>): FlowEvent(id, "Created", activityName)

internal fun FlowEvent.toModel() : FlowEventModel = when(this) {
    is Resumed -> ResumedModel(id, activityName)
    is Destroyed -> DestroyedModel(id, activityName)
    is Created -> CreatedModel(id, activityName, extras)
}