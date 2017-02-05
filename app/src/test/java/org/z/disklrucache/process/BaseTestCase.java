package org.z.disklrucache.process;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.z.disklrucache.BuildConfig;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class,
        packageName = BuildConfig.APPLICATION_ID)
public abstract class BaseTestCase {

}
