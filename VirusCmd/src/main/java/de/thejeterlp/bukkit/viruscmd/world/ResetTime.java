/*
 * Copyright 2014 TheJeterLP.
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

package de.thejeterlp.bukkit.viruscmd.world;

/**
 * @author TheJeterLP
 */
public class ResetTime implements Runnable {
    
    private final VCWorld w;
    
    public ResetTime(VCWorld w) {
        this.w = w;
    }
    
    @Override
    public void run() {
        if (w.isTimePaused()) {
            w.getWorld().setTime(w.getTimePauseMoment());
        }
    }
    
}
