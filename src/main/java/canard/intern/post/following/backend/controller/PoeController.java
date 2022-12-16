package canard.intern.post.following.backend.controller;

import canard.intern.post.following.backend.dto.PoeDetailDto;
import canard.intern.post.following.backend.dto.PoeDto;
import canard.intern.post.following.backend.service.PoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@RestController
@RequestMapping("/api/poes")
public class PoeController {

    @Autowired
    private PoeService poeService;

    /**
     * GET /api/poes
     * @return all poes
     */
    @GetMapping
    public List<PoeDto> getAll(){
        return poeService.getAll();
    }

    /**
     * GET /api/poes/{id}
     *
     * Example: in order to get poe of id 3, call
     *    GET /api/poes/3
     *
     * @param id id of poe to get
     * @return poe with this id if found
     */
    @GetMapping("/{id}")
    public PoeDetailDto getById(@PathVariable("id") int id){
        var optTraineeDto =  poeService.getById(id);
        if (optTraineeDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Poe not found with id <%d>", id));
        }
        return optTraineeDto.get();
    }

    /**
     * POST /api/poes
     * @param poeDto
     * @return poeDto saved with its id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PoeDto create(@Valid @RequestBody PoeDto poeDto) {
        return poeService.create(poeDto);
    }

    /**
     * PUT /api/poes/{id}
     * @param id id of poe to update
     * @param poeDto
     * @return poeDto updated
     */
    @PutMapping("/{id}")
    public PoeDto update(
            @PathVariable("id") int id,
            @Valid @RequestBody PoeDto poeDto
    ){
        if (Objects.nonNull(poeDto.getId()) && (poeDto.getId() != id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Id <%d> from path does not match id <%d> from body",
                            id, poeDto.getId()));
            // NB:you can use also:  MessageFormat.format or StringBuilder
        }
        return poeService.update(id, poeDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Poe not found with id <%d>",
                                id)));
    }

    //NB: other choice, return Dto removed if found
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id){
        if(!poeService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Poe not found with id <%d>",
                            id));
        }
    }
}
