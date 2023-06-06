package tech.wedev.wecom.standard.impl;

import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.standard.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestImpl implements TestService {

    //避免在方法内部调用，否则会失效
    @Override
    @StopWatch
    public void testAop() {
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
