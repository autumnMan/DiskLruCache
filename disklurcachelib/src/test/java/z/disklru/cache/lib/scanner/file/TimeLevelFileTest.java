package z.disklru.cache.lib.scanner.file;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.PriorityQueue;

import z.disklru.cache.lib.core.BaseTestCase;
import z.disklru.cache.lib.scanner.strategy.DefFileStrategy;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * Created by Administrator on 2017/2/27.
 */
public class TimeLevelFileTest extends BaseTestCase {

    @Test
    public void orderPriorityFile() throws Exception{
        final PriorityQueue<TimeLevelFile> queue = new PriorityQueue<>();

        for (int i = 0; i < 100; ++i) {
            final DefFileStrategy fileStrategy = Mockito.mock(DefFileStrategy.class);
            Mockito.when(fileStrategy.importantLevel(Mockito.any(File.class)))
                    .thenReturn((int) (Math.random() * (FileCacheStrategy.NORMAL + 1)));

            final TimeLevelFile timeLevelFile = Mockito.spy(new TimeLevelFile(new File("I am " + i), fileStrategy));
            Mockito.when(timeLevelFile.lastModifyTime())
                    .thenReturn((long) (Math.random() * 1000));

            queue.offer(timeLevelFile);
        }

        TimeLevelFile tmpCurFile = null;
        TimeLevelFile lastTmpFile = null;
        while (!queue.isEmpty()) {
            tmpCurFile = queue.poll();
            System.out.print("output : " + tmpCurFile + "\n");

            if (lastTmpFile != null) {
                //上一个先出来的，必须是level比现在这个大，或者是修改时间比现在这个小才是正确的
                boolean judge = lastTmpFile.importantLevel() >= tmpCurFile.importantLevel()
                        || lastTmpFile.lastModifyTime() <= tmpCurFile.lastModifyTime();
                Assert.assertTrue(judge);
            }
            lastTmpFile = tmpCurFile;
        }
    }

}