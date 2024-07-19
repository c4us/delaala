package io.c4us.delaalaapi.service;

import io.c4us.delaalaapi.domain.Car;
import io.c4us.delaalaapi.domain.Customer;
import io.c4us.delaalaapi.repo.CarRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.c4us.delaalaapi.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class CarService {
    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectory(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/cars/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException();
        }

    };
    @Autowired
    private CarRepo carRepo;

    public Page<Car> getAllCar(int page, int size) {

        return carRepo.findAll(PageRequest.of(page, size, Sort.by("createdDate")));

    }

    public Car getCar(String id) {
        return carRepo.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public Car delCar(String id) {
        try {
            Car car = getCar(id);
            carRepo.deleteById(id);
            return car;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public Car updateCar(Car newcar) {
        try {
            Car car = getCar(newcar.getId());
            car.setCarId(newcar.getCarId());
            carRepo.save(car);
            return car;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public Car createCar(Car car) {
        return carRepo.save(car);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Upload photo for car : {}", id);
        Car car = getCar(id);
        String photoUrl = photoFunction.apply(id, file);
        car.setCarPhotoUrl(photoUrl);
        carRepo.save(car);
        return photoUrl;

    }
}
