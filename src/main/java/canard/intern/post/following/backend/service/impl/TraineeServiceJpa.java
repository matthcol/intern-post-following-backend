package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.TraineeDetailDto;
import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.error.UpdateException;
import canard.intern.post.following.backend.repository.PoeRepository;
import canard.intern.post.following.backend.repository.TraineeRepository;
import canard.intern.post.following.backend.service.TraineeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TraineeServiceJpa implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private PoeRepository poeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TraineeDto> getAll() {
        return traineeRepository.findAll()
                .stream()
                .map(te -> modelMapper.map(te, TraineeDto.class))
                .toList();
    }

    @Override
    public Optional<TraineeDetailDto> getById(int id) {
        return traineeRepository.findById(id)
                .map(te -> modelMapper.map(te, TraineeDetailDto.class));
    }

    @Override
    public Set<TraineeDto> getByLastnameContaining(String lastnamePartial) {
        return null;
    }

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        var traineeEntity = modelMapper.map(traineeDto, Trainee.class);
        try {
            traineeRepository.save(traineeEntity);
            traineeRepository.flush(); // force SQL INSERT here
            return modelMapper.map(traineeEntity, TraineeDto.class);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Trainee cannot be saved", ex);
        }
    }

    @Override
    public Optional<TraineeDto> update(int id, TraineeDto traineeDto) {
        // in case traineeDto has no id
        traineeDto.setId(id);
        try {
            return traineeRepository.findById(id)
                    .map(te -> {
                        // update in DB if found
                        // 1 - overwrite entity fields with dto fields
                        modelMapper.map(traineeDto, te);
                        // 2 - synchronize with DB
                        traineeRepository.flush(); // force SQL UPDATE here
                        // 3 - transform back modified entity in  DTO
                        return modelMapper.map(te, TraineeDto.class);
                    });
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Trainee cannot be updated", ex);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return traineeRepository.findById(id)
                    .map(te -> {
                        // delete in DB if found
                        traineeRepository.delete(te);
                        traineeRepository.flush(); // force SQL delete here
                        return true;
                    })
                    .orElse(false);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Trainee cannot be deleted", ex);
        }
    }

    @Override
    public Optional<TraineeDetailDto> setPoe(int idTrainee, int idPoe) {
        return  traineeRepository.findById(idTrainee)
                .flatMap(traineeEntity -> poeRepository.findById(idPoe)
                        .map(poeEntity -> {
                            traineeEntity.setPoe(poeEntity);
                            traineeRepository.flush(); // force UPDATE here
                            return modelMapper.map(traineeEntity, TraineeDetailDto.class);
                        })
                );
    }
}
