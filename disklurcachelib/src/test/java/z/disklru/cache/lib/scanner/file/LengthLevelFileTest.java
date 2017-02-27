package z.disklru.cache.lib.scanner.file;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.PriorityQueue;

import z.disklru.cache.lib.core.BaseTestCase;
import z.disklru.cache.lib.scanner.file.LengthLevelFile;
import z.disklru.cache.lib.scanner.strategy.DefFileStrategy;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * Created by Mr-Z on 2017/2/26.
 */
public class LengthLevelFileTest extends BaseTestCase{

    @Test
    public void orderPriorityFile() throws Exception{
        final PriorityQueue<LengthLevelFile> queue = new PriorityQueue<>();

        for (int i = 0; i < 100; ++i) {
            final DefFileStrategy fileStrategy = Mockito.mock(DefFileStrategy.class);
            Mockito.when(fileStrategy.importantLevel(Mockito.any(File.class)))
                    .thenReturn((int) (Math.random() * (FileCacheStrategy.DO_NOT_CARE + 1)));

            final LengthLevelFile lengthLevelFile = Mockito.spy(new LengthLevelFile(new File("I am " + i), fileStrategy));
            Mockito.when(lengthLevelFile.fileSize())
                    .thenReturn((long) (Math.random() * 1000));

            queue.offer(lengthLevelFile);
        }

        LengthLevelFile tmpCurFile = null;
        LengthLevelFile lastTmpFile = null;
        while (!queue.isEmpty()) {
            tmpCurFile = queue.poll();
            System.out.print("output : " + tmpCurFile + "\n");

            if (lastTmpFile != null) {
                //上一个先出来的，必须是level比现在这个大，或者是file size比现在这个大才是正确的
                boolean judge = lastTmpFile.importantLevel() >= tmpCurFile.importantLevel()
                        || lastTmpFile.fileSize() >= tmpCurFile.fileSize();
                Assert.assertTrue(judge);
            }
            lastTmpFile = tmpCurFile;
        }
    }
}