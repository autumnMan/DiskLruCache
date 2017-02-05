package z.disklru.cache.lib.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DiskLruCacheTest extends BaseTestCase {
    private final File cacheDir = new File("/sdcard/test/");
    private final long maxSize = 10;//byte

    private final String testFileName = "test";

    private DiskLruCache mDiskLruCache;

    @Before
    public void setUp() throws Exception {
       mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, maxSize);
    }

    @After
    public void tearDown() throws Exception {
        mDiskLruCache.delete();
    }

    @Test
    public void open() throws Exception {
        Assert.assertNotNull(mDiskLruCache);
    }

    @Test
    public void get() throws Exception {
        Assert.assertNull(mDiskLruCache.get(testFileName));

        DiskLruCache.Editor editor = mDiskLruCache.edit(testFileName);
        FileUtil.writeString2File(editor.getFile(0),
                "hello..");
        editor.commit(false);

        DiskLruCache.Value value = mDiskLruCache.get(testFileName);
        Assert.assertNotNull(value);
        Assert.assertTrue(value.getFile(0).exists());
    }

    @Test
    public void editAndCheckAsync() throws Exception {
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit("test");
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit(false);

        //commit之后会异步检查当前文件夹所有文件的大小是否超出设置大小
        //在单元测试时不会等待异步操作的执行，所以此时文件依然存在
        final File readFile = mDiskLruCache.get("test").getFile(0);
        final String readMsg = FileUtil.getFileContentAsString(readFile);
        Assert.assertEquals(readMsg, content);

    }

    @Test
    public void editAndCheckSync() throws Exception {
        final String content = "Hello world! Test ten bytes";

        writeContentSyn(testFileName, content, false);

        //commit之后调用了flush会同步检查当前文件夹所有文件的大小是否超出设置大小
        //此时已经超出了设置的总大小，写入的文件应该已经被删除，故获取不到该文件的信息
        Assert.assertNull(mDiskLruCache.get(testFileName));
        //文件内的文件总大小应小于设置的总大小
        Assert.assertTrue(mDiskLruCache.size() < mDiskLruCache.getMaxSize());
    }

    @Test
    public void appendContentOverSize() throws Exception {
        writeContentSyn(testFileName, "Hello world!", true);
        writeContentSyn(testFileName, "Test ten bytes", true);

        //写入的字符串大于10个字节，此时文件应已被删除
        final DiskLruCache.Value value = mDiskLruCache.get(testFileName);
        Assert.assertNull(value);
        //文件内的文件总大小应小于设置的总大小
        Assert.assertTrue(mDiskLruCache.size() < mDiskLruCache.getMaxSize());
    }

    @Test
    public void appendContentUnderSize() throws Exception {

        writeContentSyn(testFileName, "He", true);
        writeContentSyn(testFileName, "llo", true);

        //写入的字符串小于10个字节，此时文件不会被删除
        final DiskLruCache.Value value = mDiskLruCache.get(testFileName);
        Assert.assertNotNull(value);
        Assert.assertTrue(value.getFile(0).exists());
        //文件内容应该和写入的一致
        Assert.assertEquals("Hello", FileUtil.getFileContentAsString(value.getFile(0)));
        //文件内的文件总大小应小于设置的总大小
        Assert.assertTrue(mDiskLruCache.size() < mDiskLruCache.getMaxSize());
    }

    private void writeContentSyn(String key, String he, boolean append) throws IOException {
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, he);
        //commit只是提交内容，未检查提交内容后是否超出了限制的大小
        editor.commit(append);
        //调用flush相当于立刻检查当前文件夹内的文件大小是否超出总大小
        //若超出则按照最早访问到最后访问的文件顺序来删除文件，直到所有文件的总大小小于设置的总大小
        mDiskLruCache.flush();
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

        writeContentSyn(testFileName, content, false);

        DiskLruCache.Editor editor = mDiskLruCache.edit(testFileName + 1);
        final File file1 = editor.getFile(0);
        FileUtil.writeString2File(file1, content);
        editor.commit(false);
        mDiskLruCache.flush();

        Assert.assertEquals(content.length() * 2, mDiskLruCache.size());
    }

    @Test
    public void remove() throws Exception {
        final String content = "Hello world! Test ten bytes";

        DiskLruCache.Editor editor = mDiskLruCache.edit(testFileName);
        final File file = editor.getFile(0);
        FileUtil.writeString2File(file, content);
        editor.commit(false);

        mDiskLruCache.remove(testFileName);

        Assert.assertNull(mDiskLruCache.get(testFileName));
    }

    @Test
    public void isClosed() throws Exception {
        Assert.assertFalse(mDiskLruCache.isClosed());
        mDiskLruCache.close();
        Assert.assertTrue(mDiskLruCache.isClosed());
    }

    @Test
    @Ignore(value = "暂时不用")
    public void flush() throws Exception {

    }

    @Test
    @Ignore(value = "暂时不用")
    public void close() throws Exception {

    }

    @Test
    public void delete() throws Exception {
        mDiskLruCache.setMaxSize(1000);
        final String content = "Hello world! Test ten bytes";

        writeContentSyn(testFileName, content, false);

        Assert.assertFalse(mDiskLruCache.isClosed());
        Assert.assertTrue(mDiskLruCache.size() < mDiskLruCache.getMaxSize());

        mDiskLruCache.delete();
        Assert.assertTrue(mDiskLruCache.isClosed());
    }

}