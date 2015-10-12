package net.sblundy.experiments.ember.data;

import net.sblundy.experiments.ember.model.AuthorRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 */
public interface AuthorRepository extends CrudRepository<AuthorRecord, UUID> {
    AuthorRecord findByFullName(String fullName);
}
