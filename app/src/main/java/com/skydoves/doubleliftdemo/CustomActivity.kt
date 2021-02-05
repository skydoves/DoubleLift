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
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.doubleliftdemo.databinding.ActivityCustomBinding

class CustomActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = ActivityCustomBinding.inflate(layoutInflater)
    setContentView(binding.root)

    with(binding) {
      doubleLiftLayout.setOnClickListener {
        if (doubleLiftLayout.isExpanded) {
          doubleLiftLayout.collapse()
        } else {
          doubleLiftLayout.expand()
        }
      }

      doubleLiftLayout2.setOnClickListener {
        if (doubleLiftLayout2.isExpanded) {
          doubleLiftLayout2.collapse()
        } else {
          doubleLiftLayout2.expand()
        }
      }

      doubleLiftLayout3.setOnClickListener {
        if (doubleLiftLayout3.isExpanded) {
          doubleLiftLayout3.collapse()
        } else {
          doubleLiftLayout3.expand()
        }
      }
    }
  }
}
