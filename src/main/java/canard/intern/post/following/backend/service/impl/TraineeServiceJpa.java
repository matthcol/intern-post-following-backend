package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.repository.TraineeRepository;
import canard.intern.post.following.backend.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TraineeServiceJpa implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Override
    public List<TraineeDto> getAll() {
        var traineeEntities = traineeRepository.findAll();
        // TODO: use modelmapper
        return traineeEntities.stream()
                .map(te -> TraineeDto.builder()
                        .id(te.getId())
                        .lastname(te.getLastname())
                        // ...
                        .build())
                .toList();
    }

    @Override
    public Optional<TraineeDto> getById(int id) {
        return Optional.empty();
    }

    @Override
    public Set<TraineeDto> getByLastnameContaining(String lastnamePartial) {
        return null;
    }

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        return null;
    }

    @Override
    public Optional<TraineeDto> update(int id, TraineeDto traineeDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
