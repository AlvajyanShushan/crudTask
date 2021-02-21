package egs.task.services;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.AbstractEntity;
import egs.task.repositories.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>> implements CommonService<E> {

    private final R repository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E save(E model) throws EntityNotFoundException {
        if (model.getHidden() == null)
            model.setHidden(false);
        return repository.save(model);
    }

    @Override
    public Iterable<E> saveAll(Iterable<E> iterable) {
        return repository.saveAll(iterable);
    }

    @Override
    public Iterable<E> listAll() {
        return repository.findAllByHiddenFalse();
    }

    @Override
    public Page<E> listAll(Pageable pageable) {
        return repository.findAllByHiddenFalse(pageable);
    }

    @Override
    public E getById(Long id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty() || repository.findById(id).get().getHidden().equals(true))
            throw new EntityNotFoundException(repository.findById(id).getClass(), "id", String.valueOf(id));

        return repository.findById(id).orElse(null);
    }

    @Override
    public E getByIdWithHidden(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void remove(E entity) {
        repository.delete(entity);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public boolean delete(Long id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFoundException(repository.findById(id).getClass(), "id", String.valueOf(id));
        repository.deleteById(id);
        return true;
    }
}