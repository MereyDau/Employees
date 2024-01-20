package com.example.workers;

import com.example.workers.model.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootApplication
public class WorkersApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WorkersApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Employee employee1 = new Employee();
        employee1.setName("Merey");
        employee1.setPosition("Web Developer");
        employee1.setEmail("mdauletkan@inbox.ru");


        Employee employee2 = new Employee();
        employee2.setName("Sergei");
        employee2.setPosition("Driver");
        employee2.setEmail("sergei@email.com");

        saveEmployee(employee1);
        saveEmployee(employee2);
    }

    private void saveEmployee(Employee employee) {
        String url = "jdbc:mysql://localhost:3306/workers?useSSL=false";
        String user = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO workers.employees (name, position, email) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, employee.getName());
                preparedStatement.setString(2, employee.getPosition());
                preparedStatement.setString(3, employee.getEmail());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
