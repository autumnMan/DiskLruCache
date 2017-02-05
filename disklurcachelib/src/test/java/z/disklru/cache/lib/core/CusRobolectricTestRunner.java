package z.disklru.cache.lib.core;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Mr.Z on 2017/1/22.
 */

public class CusRobolectricTestRunner extends RobolectricTestRunner{
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public CusRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
//        System.setProperty("robolectric.offline", "true");
    }
}
