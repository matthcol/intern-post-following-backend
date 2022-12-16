package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.PoeDto;
import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.entity.Poe;
import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.error.UpdateException;
import canard.intern.post.following.backend.repository.PoeRepository;
import canard.intern.post.following.backend.service.PoeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoeServiceImpl implements PoeService {

    @Autowired
    private PoeRepository poeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PoeDto> getAll() {
        return poeRepository.findAll()
                .stream()
                .map(te -> modelMapper.map(te, PoeDto.class))
                .toList();
    }

    @Override
    public Optional<PoeDto> getById(int id) {
        return poeRepository.findById(id)
                .map(te -> modelMapper.map(te, PoeDto.class));
    }

    @Override
    public List<PoeDto> getByTitle(String title) {
        // TODO
        return List.of();
    }

    @Override
    public List<PoeDto> getByStartingYear(int year) {
        // TODO
        return List.of();
    }

    @Override
    public PoeDto create(PoeDto poeDto) {
        var poeEntity = modelMapper.map(poeDto, Poe.class);
        try {
            poeRepository.save(poeEntity);
            poeRepository.flush(); // force SQL INSERT here
            return modelMapper.map(poeEntity, PoeDto.class);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Poe cannot be saved", ex);
        }
    }

    @Override
    public Optional<PoeDto> update(int id, PoeDto poeDto) {
        poeDto.setId(id);
        try {
            return poeRepository.findById(id)
                    .map(te -> {
                        // update in DB if found
                        // 1 - overwrite entity fields with dto fields
                        modelMapper.map(poeDto, te);
                        // 2 - synchronize with DB
                        poeRepository.flush(); // force SQL UPDATE here
                        // 3 - transform back modified entity in  DTO
                        return modelMapper.map(te, PoeDto.class);
                    });
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Trainee cannot be saved", ex);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return poeRepository.findById(id)
                    .map(te -> {
                        // delete in DB if found
                        poeRepository.delete(te);
                        poeRepository.flush(); // force SQL delete here
                        return true;
                    })
                    .orElse(false);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Trainee cannot be saved", ex);
        }
    }
}
