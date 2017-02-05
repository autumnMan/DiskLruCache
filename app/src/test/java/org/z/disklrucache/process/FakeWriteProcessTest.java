package org.z.disklrucache.process;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FakeWriteProcessTest extends BaseTestCase {
    @Test
    public void doWrite() throws Exception {
        final String testMsg = "Hello world!";
        final FakeWriteProcess.CusWriteTask task = Mockito.mock(FakeWriteProcess.CusWriteTask.class);
        final ArgumentCaptor<FakeWriteProcess.DefWriteListener> listener = ArgumentCaptor.forClass(FakeWriteProcess.DefWriteListener.class);
        final FakeWriteProcess fakeWriteProcess = new FakeWriteProcess();
        fakeWriteProcess.setWriteTask(task);
        fakeWriteProcess.doWrite(testMsg, new FakeWriteProcess.DefWriteListener());

        Mockito.verify(task).setListener(listener.capture());

        listener.getValue().onSuccess(testMsg);
        Assert.assertEquals(testMsg, (listener.getValue().mMsg));
    }

    @Test
    public void doWrite2() throws Exception {
        final String testMsg = "Hello world!";
        final FakeWriteProcess fakeWriteProcess = Mockito.mock(FakeWriteProcess.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                FakeWriteProcess.WriteListener listener = (FakeWriteProcess.WriteListener) invocation.getArguments()[1];
                listener.onSuccess((String) invocation.getArguments()[0]);
//                listener.onSuccess("doing a fake answer");
                return null;
            }
        }).when(fakeWriteProcess).doWrite2(Mockito.anyString(),
                Mockito.any(FakeWriteProcess.WriteListener.class));

        FakeWriteProcess.DefWriteListener listener = new FakeWriteProcess.DefWriteListener();
        fakeWriteProcess.doWrite2(testMsg, listener);
        Assert.assertEquals(testMsg, listener.mMsg);
    }

}