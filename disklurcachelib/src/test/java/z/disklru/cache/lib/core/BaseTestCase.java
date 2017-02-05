package z.disklru.cache.lib.core;

import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import z.disklru.cache.lib.BuildConfig;

@RunWith(CusRobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class,
        packageName = BuildConfig.APPLICATION_ID)
public abstract class BaseTestCase {

}
