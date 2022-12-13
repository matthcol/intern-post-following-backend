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
        var traineeEntities =  traineeRepository.findAll();
        // TODO: convert entities => DTO
        return null;
    }

    @Override
    public Optional<TraineeDto> getById(int id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Set<TraineeDto> getByLastnameContaining(String lastnamePartial) {
        // TODO
        return null;
    }

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        // TODO
        return null;
    }

    @Override
    public Optional<TraineeDto> update(int id, TraineeDto traineeDto) {
        // TODO
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        // TODO
        return false;
    }
}
