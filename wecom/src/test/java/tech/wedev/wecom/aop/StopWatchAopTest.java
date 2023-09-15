package tech.wedev.wecom.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tech.wedev.wecom.controller.TestController;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({StopWatchAop.class})
public class StopWatchAopTest {

    @InjectMocks
    private StopWatchAop stopWatchAop;

    @Mock
    private Signature signature;

    @Test
    public void doAroundTest() throws Throwable {
        ProceedingJoinPoint mock = Mockito.mock(ProceedingJoinPoint.class);
        PowerMockito.when(mock.getTarget()).thenReturn(TestController.class);
        PowerMockito.when(mock.getSignature()).thenReturn(signature);
        PowerMockito.when(mock.proceed()).thenReturn("pjp");
        Assert.assertNotNull(stopWatchAop.doAround(mock));
    }
}