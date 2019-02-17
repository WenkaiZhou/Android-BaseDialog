/*
 * Copyright (c) 2019 Kevin zhou
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
package com.kevin.dialog

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape

/**
 * CircleDrawable
 *
 * @author zhouwenkai@baidu.com, Created on 2019-02-17 21:09:56
 *         Major Function：<b>CircleDrawable</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */
class CircleDrawable(
    backgroundColor: Int,
    leftTopRadius: Int,
    rightTopRadius: Int,
    rightBottomRadius: Int,
    leftBottomRadius: Int
) : ShapeDrawable() {

    constructor(backgroundColor: Int, radius: Int) : this(backgroundColor, radius, radius, radius, radius)

    init {
        paint.color = backgroundColor
        shape = getRoundRectShape(leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius)
    }

    private fun getRoundRectShape(leftTop: Int, rightTop: Int, rightBottom: Int, leftBottom: Int): RoundRectShape {
        val outerRadii = FloatArray(8)
        if (leftTop > 0) {
            outerRadii[0] = leftTop.toFloat()
            outerRadii[1] = leftTop.toFloat()
        }
        if (rightTop > 0) {
            outerRadii[2] = rightTop.toFloat()
            outerRadii[3] = rightTop.toFloat()
        }
        if (rightBottom > 0) {
            outerRadii[4] = rightBottom.toFloat()
            outerRadii[5] = rightBottom.toFloat()
        }
        if (leftBottom > 0) {
            outerRadii[6] = leftBottom.toFloat()
            outerRadii[7] = leftBottom.toFloat()
        }
        return RoundRectShape(outerRadii, null, null)
    }

}