package example.twopc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class GenericallyNamedServciceImpl {

    private static final Logger LOG = LogManager.getLogger(GenericallyNamedServciceImpl.class);

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private JmsOperations jmsOperations;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Transactional(propagation = Propagation.REQUIRED)
    public void doSomething() {

        LOG.debug("Sending JMS Message");
        jmsOperations.convertAndSend("QUEUE1", "Test Message");

        LOG.debug("Writing to Database");
        jdbcOperations.execute("INSERT INTO PERSON (ID, NAME) VALUES (PERSON_SEQ.NEXTVAL, 'Goofy')");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void doSomethingBad() {

        LOG.debug("Sending JMS Message");
        jmsOperations.convertAndSend("QUEUE1", "Test Message");

        LOG.debug("Writing to Database");
        // this will cause Unique Key Constraint violation
        jdbcOperations.execute("INSERT INTO PERSON (ID, NAME) VALUES (PERSON_SEQ.NEXTVAL, 'Mickey')");

    }

    @Transactional(propagation = Propagation.NEVER)
    public void doSomethingBadWithoutDistributedTransaction() {

        {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setName("mq-only");
            new TransactionTemplate(transactionManager, definition).execute(
                    new TransactionCallbackWithoutResult() {
                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            LOG.debug("Sending JMS Message");
                            jmsOperations.convertAndSend("QUEUE1", "Test Message");
                        }
                    }
            );
        }

        {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setName("db-only");
            new TransactionTemplate(transactionManager, definition).execute(
                    new TransactionCallbackWithoutResult() {
                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            LOG.debug("Writing to Database");
                            // this will cause Unique Key Constraint violation
                            jdbcOperations.execute("INSERT INTO PERSON (ID, NAME) VALUES (PERSON_SEQ.NEXTVAL, 'Mickey')");
                        }
                    }
            );
        }

    }
}
