/*
 * Copyright 2017 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mk.gdx.firebase;

import mk.gdx.firebase.distributions.CrashDistribution;

/**
 * Gets access to Firebase Crash API in multi-modules.
 *
 * @see CrashDistribution
 * @see PlatformDistributor
 */
public class GdxFIRCrash extends PlatformDistributor<CrashDistribution> implements CrashDistribution {

    private static volatile GdxFIRCrash instance;

    /**
     * GdxFIRCrash protected constructor.
     * <p>
     * Instance of this class should be getting by {@link #instance()}
     * <p>
     * {@link PlatformDistributor#PlatformDistributor()}
     */
    private GdxFIRCrash() {
    }

    /**
     * @return Thread-safe singleton instance of this class.
     */
    public static GdxFIRCrash instance() {
        GdxFIRCrash result = instance;
        if (result == null) {
            synchronized (GdxFIRCrash.class) {
                result = instance;
                if (result == null) {
                    instance = result = new GdxFIRCrash();
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String message) {
        platformObject.log(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        platformObject.initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIOSClassName() {
        return "mk.gdx.firebase.ios.crash.Crash";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAndroidClassName() {
        return "mk.gdx.firebase.android.crash.Crash";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWebGLClassName() {
        return "mk.gdx.firebase.html.crash.Crash";
    }
}
