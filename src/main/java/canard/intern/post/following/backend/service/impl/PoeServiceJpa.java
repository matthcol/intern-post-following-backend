package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.PoeDetailDto;
import canard.intern.post.following.backend.dto.PoeDto;
import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.entity.Poe;
import canard.intern.post.following.backend.error.UpdateException;
import canard.intern.post.following.backend.repository.PoeRepository;
import canard.intern.post.following.backend.repository.TraineeRepository;
import canard.intern.post.following.backend.service.PoeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoeServiceJpa implements PoeService {

    @Autowired
    private PoeRepository poeRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PoeDto> getAll() {
        return poeRepository.findAll()
                .stream()
                .map(pe -> modelMapper.map(pe, PoeDto.class))
                .toList();
    }

    @Override
    public Optional<PoeDetailDto> getById(int id) {
        return poeRepository.findById(id)
                .map(poeEntity -> {
                    // list of trainees from this poe
                    var trainees = traineeRepository.findByPoeId(id)
                            .stream()
                            .map(traineeEntity -> modelMapper.map(traineeEntity, TraineeDto.class))
                            .toList();
                    var poeDetailDto =  modelMapper.map(poeEntity, PoeDetailDto.class);
                    poeDetailDto.setTrainees(trainees);
                    return poeDetailDto;
                });
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
                    .map(pe -> {
                        // update in DB if found
                        // 1 - overwrite entity fields with dto fields
                        modelMapper.map(poeDto, pe);
                        // 2 - synchronize with DB
                        poeRepository.flush(); // force SQL UPDATE here
                        // 3 - transform back modified entity in  DTO
                        return modelMapper.map(pe, PoeDto.class);
                    });
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Poe cannot be updated", ex);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return poeRepository.findById(id)
                    .map(pe -> {
                        // if found
                        // 1. set poe to null for each trainee of this poe
                        traineeRepository.findByPoeId(pe.getId())
                                .forEach(te -> te.setPoe(null));
                        // 2. delete poe
                        poeRepository.delete(pe);
                        // 3. sync with DB (UPDATE trainee, DELETE poe)
                        poeRepository.flush();
                        return true;
                    })
                    .orElse(false);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Poe cannot be deleted", ex);
        }
    }
}
