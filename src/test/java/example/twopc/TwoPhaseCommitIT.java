package example.twopc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-app-test.xml"})
public class TwoPhaseCommitIT {

    private static final Logger LOG = LogManager.getLogger(TwoPhaseCommitIT.class);

    @Autowired
    private GenericallyNamedServciceImpl target;

    @Test
    public void test_something() throws Exception {
        target.doSomething();
    }

    @Test(expected = Exception.class)
    public void test_something_bad() throws Exception {
        target.doSomethingBad();
    }

    @Test(expected = Exception.class)
    public void test_something_bad_no_distributed_trasnaction() throws Exception {
        target.doSomethingBadWithoutDistributedTransaction();
    }
}
