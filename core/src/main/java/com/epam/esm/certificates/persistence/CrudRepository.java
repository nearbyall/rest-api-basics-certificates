package com.epam.esm.certificates.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for CRUD operations.
 * @param <T> Object type for performing operations
 * @param <Id> Primary key
 */
public interface CrudRepository<T, Id> {

    /**
     * Creates a new instance of an entity in the database.
     * @param obj Object to create
     * @return optional id of created object
     */
    Optional<Id> create(T obj);

    /**
     * Gets all existing entities with provided type.
     * @return list of entities
     */
    List<T> getAll();

    /**
     * Gets entity with the provided id.
     * @param id id of the needed object
     * @return optional object of provided type
     */
    Optional<T> getById(Id id);

    /**
     * Updates provided entity object.
     * @param obj entity with fields that needed to be updated
     * @return the number of rows affected
     */
    int update(T obj);

    /**
     * Deletes entity with the provided id.
     * @param id id of the object to be deleted
     * @return the number of rows affected
     */
    int delete(Id id);

}
