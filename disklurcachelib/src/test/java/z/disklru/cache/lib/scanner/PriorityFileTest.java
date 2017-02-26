package z.disklru.cache.lib.scanner;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.PriorityQueue;

import z.disklru.cache.lib.core.BaseTestCase;

/**
 * Created by Mr-Z on 2017/2/26.
 */
public class PriorityFileTest extends BaseTestCase{

    @Test
    public void orderPriorityFile() throws Exception{
        final PriorityQueue<PriorityFile> queue = new PriorityQueue<>();

        for (int i = 0; i < 1000; ++i) {
            final DefFileStrategy fileStrategy = Mockito.mock(DefFileStrategy.class);
            Mockito.when(fileStrategy.importantLevel(Mockito.any(File.class)))
                    .thenReturn((int) (Math.random() * (FileCacheStrategy.DO_NOT_CARE + 1)));

            final PriorityFile priorityFile = Mockito.spy(new PriorityFile(new File("I am " + i), fileStrategy));
            Mockito.when(priorityFile.fileSize())
                    .thenReturn((long) (Math.random() * 1000));

            queue.offer(priorityFile);
        }

        PriorityFile tmpCurFile = null;
        PriorityFile lastTmpFile = null;
        while (!queue.isEmpty()) {
            tmpCurFile = queue.poll();
//            System.out.print("output : " + tmpCurFile + "\n");

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