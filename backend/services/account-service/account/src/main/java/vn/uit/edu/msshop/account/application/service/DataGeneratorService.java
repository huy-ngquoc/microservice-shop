package vn.uit.edu.msshop.account.application.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;

import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;

//@Component

public class DataGeneratorService {
    private final Random random;
    private final CreateAccountUseCase createUseCase;
    private final AccountWebMapper mapper;
    private int COUNT = 0;

    public DataGeneratorService(
            CreateAccountUseCase createUseCase,
            AccountWebMapper mapper) {
        this.random = new Random();
        this.createUseCase = createUseCase;
        this.mapper = mapper;
    }

    @Scheduled(
            fixedRate = 2000)
    public void generateRegistrationData() {
        if (COUNT >= 1000) {
            System.out.println("Da du user");
            return;
        }
        String name = "user_" + UUIDs.newId().toString().substring(0, 5);
        String password = UUIDs.newId().toString().substring(0, 10);
        String role = random.nextBoolean() ? "Client_User" : "Client_Admin";
        String phone = "0" + (100000000 + random.nextInt(900000000));
        String email = name + "@gmail.com";
        String firstName = "name";
        String lastName = "name";
        CreateAccountRequest request = new CreateAccountRequest(name, email, password, role, email, phone, phone,
                firstName, lastName);

        try (FileWriter writer = new FileWriter("data.txt", true)) {
            createUseCase.create(mapper.toCommand(request));
            writer.write(name + "," + password + "\n");
            System.out.println("Đã lưu: " + name + " | Role: " + role);
            COUNT++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
