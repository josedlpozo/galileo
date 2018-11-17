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

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.josedlpozo.galileo.R

internal sealed class FlowEventModel(open val id: Long,
                                     val name: String,
                                     open val activityName: String, @DrawableRes val background: Int, @ColorRes val color: Int)

internal data class ResumedModel(override val id: Long, override val activityName: String) : FlowEventModel(id,
                                                                                                            "Resumed",
                                                                                                            activityName,
                                                                                                            R.drawable.rounded_corner,
                                                                                                            R.color.galileo_resumed_event)

internal data class DestroyedModel(override val id: Long, override val activityName: String) : FlowEventModel(id,
                                                                                                              "Destroyed",
                                                                                                              activityName,
                                                                                                              R.drawable.rounded_corner,
                                                                                                              R.color.galileo_destroy_event)

internal data class CreatedModel(override val id: Long, override val activityName: String, val extras: List<BundleItem>) : FlowEventModel(id,
                                                                                                                                          "Created",
                                                                                                                                          activityName,
                                                                                                                                          R.drawable.rounded_corner,
                                                                                                                                          R.color.galileo_create_event)
