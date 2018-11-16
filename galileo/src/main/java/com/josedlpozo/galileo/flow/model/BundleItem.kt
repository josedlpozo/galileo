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

import android.os.Bundle

internal data class BundleItem(val key: String, val value: Any)

internal val Bundle?.items: List<BundleItem>
    get() = this?.let { bundle ->
        bundle.keySet().map {
            BundleItem(it, bundle[it])
        }
    } ?: listOf()