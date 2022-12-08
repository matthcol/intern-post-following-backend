package canard.intern.post.following.backend.controller;

import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.enums.Gender;
import canard.intern.post.following.backend.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    /**
     * GET /api/trainees
     * @return all trainees
     */
    @GetMapping
    public List<TraineeDto> getAll(){
       return traineeService.getAll();
    }

    /**
     * GET /api/trainees/{id}
     *
     * Example: in order to get trainee of id 3, call
     *    GET /api/trainees/3
     *
     * @param id id of trainee to get
     * @return trainee with this id if found
     */
    @GetMapping("/{id}")
    public TraineeDto getById(@PathVariable("id") int id){
        var optTraineeDto =  traineeService.getById(id);
        if (optTraineeDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No trainee found with id <%d>", id));
        }
        return optTraineeDto.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TraineeDto create(@Valid @RequestBody TraineeDto traineeDto) {
        traineeDto.setId(10);
        return traineeDto;
    }

    @PutMapping("/{id}")
    public TraineeDto update(
            @PathVariable("id") int id,
            @Valid @RequestBody TraineeDto traineeDto
    ){
        if (Objects.nonNull(traineeDto.getId()) && (traineeDto.getId() != id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Id <%d> from path does not match id <%d> from body",
                            id, traineeDto.getId()));
            // NB:you can use also:  MessageFormat.format or StringBuilder
        }
        traineeDto.setId(id);
        return traineeDto;
    }

    //NB: other choice, return Dto removed if found
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id){
        // TODO: remove Trainee with this id
    }
}
