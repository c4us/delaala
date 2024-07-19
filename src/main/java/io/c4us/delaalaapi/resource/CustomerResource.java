package io.c4us.delaalaapi.resource;

import io.c4us.delaalaapi.domain.Customer;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerResource {
    private  final CustomerService  customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("/customers/userID")).body(customerService.createCustomer(customer));
    }

    @PostMapping("/update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("/customers/update/userID")).body(customerService.updateCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getCustomers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(customerService.getAllCustomers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(customerService.getCustomer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> delCustomer(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(customerService.delCustomer(id));
    }


    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(customerService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
