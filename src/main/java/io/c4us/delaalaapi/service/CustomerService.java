package io.c4us.delaalaapi.service;

import io.c4us.delaalaapi.constant.CustomersStatus;
import io.c4us.delaalaapi.domain.Customer;
import io.c4us.delaalaapi.repo.CustomerRepo;
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
public class CustomerService {
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
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/customers/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException();
        }

    };
    @Autowired
    private CustomerRepo customerRepo;

    public Page<Customer> getAllCustomers(int page, int size) {
        return customerRepo.findAll(PageRequest.of(page, size, Sort.by("customerName")));
    }

    public Customer getCustomer(String id) {
        return customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer delCustomer(String id) {
        try {
            Customer customer = getCustomer(id);
            customerRepo.deleteById(id);
            return customer;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public Customer updateCustomer(Customer newCustomer) {
        try {
            Customer customer = getCustomer(newCustomer.getId());
            customer.setCustomerName(newCustomer.getCustomerName());
            customer.setCustomerType(newCustomer.getCustomerType());
            customer.setCustomerPhoneNumber(newCustomer.getCustomerPhoneNumber());
            customerRepo.save(customer);
            return customer;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }


    public Customer createCustomer(Customer customer) {
        customer.setIsActive(CustomersStatus.ACTIVE);
        return customerRepo.save(customer);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Upload photo to Customer : {}", id);
        Customer customer = getCustomer(id);
        String photoUrl = photoFunction.apply(id, file);
        customer.setCarPhotoUrl(photoUrl);
        customerRepo.save(customer);

        return photoUrl;

    }
}
