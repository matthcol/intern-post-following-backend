package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.repository.TraineeRepository;
import canard.intern.post.following.backend.service.TraineeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TraineeServiceJpa implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

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
