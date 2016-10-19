package example.twopc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenericallyNamedServciceImpl {

    private static final Logger LOG = LogManager.getLogger(GenericallyNamedServciceImpl.class);

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private JmsOperations jmsOperations;

    public void doSomething() {

        LOG.debug("Sending JMS Message");
        jmsOperations.convertAndSend("QUEUE1", "Test Message");

        LOG.debug("Writing to Database");
        jdbcOperations.execute("INSERT INTO PERSON (ID, NAME) VALUES (PERSON_SEQ.NEXTVAL, 'Goofy')");

    }

    public void doSomethingBad() {

        LOG.debug("Sending JMS Message");
        jmsOperations.convertAndSend("QUEUE1", "Test Message");

        LOG.debug("Writing to Database");
        jdbcOperations.execute("INSERT INTO PERSON (ID, NAME) VALUES (PERSON_SEQ.NEXTVAL, 'Mickey')");

    }
}
