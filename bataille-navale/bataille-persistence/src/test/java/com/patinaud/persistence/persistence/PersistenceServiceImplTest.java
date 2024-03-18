package com.patinaud.persistence.persistence;

import com.patinaud.bataillepersistence.persistence.PersistenceServiceImpl;
import com.patinaud.persistence.config.TestDatabaseConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDatabaseConfiguration.class})
@DirtiesContext
@Transactional
public class PersistenceServiceImplTest {

    @Autowired
    PersistenceServiceImpl persistenceServiceImpl;

    /*
        @Autowired
        GameRepository gameRepository;
    */
    @Test
    void initialize() {
        persistenceServiceImpl.initializeGame("ABCDE");
        //  Assertions.assertEquals(1, gameRepository.findAll().size());
    }

}
