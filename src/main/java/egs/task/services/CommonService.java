package egs.task.services;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommonService<E extends AbstractEntity> {

    E save(E entity) throws EntityNotFoundException;

    Iterable<E> saveAll(Iterable<E> iterable);

    Iterable<E> listAll();

    Page<E> listAll(Pageable pageable);

    E getById(Long id) throws EntityNotFoundException;

    E getByIdWithHidden(Long id) throws EntityNotFoundException;

    boolean delete(Long id) throws Exception;

    void remove(E entity);

    Long count();
}