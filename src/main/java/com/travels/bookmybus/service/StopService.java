package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.StopDto;
import com.travels.bookmybus.mapper.StopMapper;
import com.travels.bookmybus.model.Stop;
import com.travels.bookmybus.repository.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class StopService {
    private final StopRepository stopRepository;
    public StopDto saveStop(StopDto stopDto){
        Stop receivedStop = StopMapper.toMapEntity(stopDto);
        return StopMapper.toMapDto(stopRepository.save(receivedStop));
    }
    public StopDto getById(Long id){
        Optional<Stop> receivedStop = stopRepository.findById(id);
        return receivedStop.map(StopMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Given Stop Id not found"));
    }
    public List<StopDto> getAll(){
        List<Stop> receivedStops = stopRepository.findAll();
        if(receivedStops.isEmpty())
            throw new NoSuchElementException("Stop details are not added in the database");
        return receivedStops.stream().map(StopMapper::toMapDto).toList();
    }
}
