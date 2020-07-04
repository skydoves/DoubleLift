/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.doubleliftdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.cardView0
import kotlinx.android.synthetic.main.activity_main.cardView1
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout2
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout3
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout4
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout5
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout6
import kotlinx.android.synthetic.main.activity_main.doubleLiftLayout7

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    cardView0.setOnClickListener {
      if (!doubleLiftLayout.isExpanded) {
        doubleLiftLayout.expand()
        doubleLiftLayout2.expand()
        doubleLiftLayout3.expand { Toast.makeText(this, "expanded", Toast.LENGTH_SHORT).show() }
      } else {
        doubleLiftLayout.collapse()
        doubleLiftLayout2.collapse()
        doubleLiftLayout3.collapse { Toast.makeText(this, "collapsed", Toast.LENGTH_SHORT).show() }
      }
    }

    cardView1.setOnClickListener {
      if (!doubleLiftLayout4.isExpanded) {
        doubleLiftLayout4.expand()
        doubleLiftLayout5.expand()
        doubleLiftLayout6.expand()
        doubleLiftLayout7.expand { Toast.makeText(this, "expanded", Toast.LENGTH_SHORT).show() }
      } else {
        doubleLiftLayout4.collapse()
        doubleLiftLayout5.collapse()
        doubleLiftLayout6.collapse()
        doubleLiftLayout7.collapse { Toast.makeText(this, "collapsed", Toast.LENGTH_SHORT).show() }
      }
    }
  }
}
