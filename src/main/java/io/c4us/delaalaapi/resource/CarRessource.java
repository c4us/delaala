package io.c4us.delaalaapi.resource;


import io.c4us.delaalaapi.domain.Car;
import io.c4us.delaalaapi.service.CarService;
import io.c4us.delaalaapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.c4us.delaalaapi.constant.Constant.PHOTO_DIRECTORY;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarRessource {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        try {
            return ResponseEntity.created(URI.create("/cars/userID")).body(carService.createCar(car));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        return ResponseEntity.created(URI.create("/cars/update/userID")).body(carService.updateCar(car));
    }

    @GetMapping
    public ResponseEntity<Page<Car>> getCar(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(carService.getAllCar(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(carService.getCar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> delCar(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(carService.delCar(id));
    }


    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(carService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
