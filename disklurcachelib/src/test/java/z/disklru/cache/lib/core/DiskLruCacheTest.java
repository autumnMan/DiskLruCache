package z.disklru.cache.lib.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;


public class DiskLruCacheTest extends BaseTestCase {
    private final File cacheDir = new File("/sdcard/test/");
    private final long maxSize = 10;//byte

    private DiskLruCache mDiskLruCache;

    @Before
    public void setUp() throws Exception {
       mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, maxSize);
    }

    @After
    public void tearDown() throws Exception {
        mDiskLruCache.delete();
        mDiskLruCache.close();
    }

    @Test
    public void open() throws Exception {
        Assert.assertNotNull(mDiskLruCache);
    }

    @Test
    public void get() throws Exception {
        Assert.assertNull(mDiskLruCache.get("00"));
    }

    @Test
    public void edit() throws Exception {
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit("test");
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit();

        final File readFile = mDiskLruCache.get("test").getFile(0);
        final String readMsg = FileUtil.getFileContentAsString(readFile);
        Assert.assertEquals(readMsg, content);

    }

    @Test
    public void getDirectory() throws Exception {
        Assert.assertNotNull(mDiskLruCache.getDirectory());
        Assert.assertEquals(cacheDir.getAbsolutePath(), mDiskLruCache.getDirectory().getAbsolutePath());
    }

    @Test
    public void getMaxSize() throws Exception {
        Assert.assertEquals(mDiskLruCache.getMaxSize(), maxSize);
    }

    @Test
    public void setMaxSize() throws Exception {
        final long tmpMaxSize = 1000;
        mDiskLruCache.setMaxSize(tmpMaxSize);
        Assert.assertEquals(tmpMaxSize, mDiskLruCache.getMaxSize());
    }

    @Test
    public void size() throws Exception {
        mDiskLruCache.setMaxSize(1000);
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit("test");
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit();

        editor = mDiskLruCache.edit("test1");
        final File file1 = editor.getFile(0);
        FileUtil.writeString2File(file1, content);
        editor.commit();

        Assert.assertEquals(content.length() * 2, mDiskLruCache.size());
    }

    @Test
    public void remove() throws Exception {
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit("test");
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit();

        mDiskLruCache.remove("test");

        Assert.assertNull(mDiskLruCache.get("test"));
    }

    @Test
    public void isClosed() throws Exception {
        Assert.assertFalse(mDiskLruCache.isClosed());
        mDiskLruCache.close();
        Assert.assertTrue(mDiskLruCache.isClosed());
    }

    @Test
    public void flush() throws Exception {

    }

    @Test
    public void close() throws Exception {

    }

    @Test
    public void delete() throws Exception {
        mDiskLruCache.setMaxSize(1000);
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit("test");
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit();

        Assert.assertFalse(mDiskLruCache.isClosed());
        Assert.assertTrue(mDiskLruCache.size() < mDiskLruCache.getMaxSize());

        mDiskLruCache.delete();
        Assert.assertTrue(mDiskLruCache.isClosed());
    }

}