package com.example.workers.controller;

import com.example.workers.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final String URL = "jdbc:mysql://localhost:3306/workers?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    @GetMapping
    public List<Employee> getAllEmployees() {
        return getEmployees();
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        saveEmployee(employee);
        return employee;
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        Employee employee = findEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee updatedEmployee) {
        Employee existingEmployee = findEmployeeById(id);

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setPosition(updatedEmployee.getPosition());
        existingEmployee.setEmail(updatedEmployee.getEmail());

        updateEmployee(existingEmployee);

        return ResponseEntity.ok(existingEmployee);
    }

    @DeleteMapping("{id}")
    public String deleteEmployeeById(@PathVariable long id) {
        Employee employee = findEmployeeById(id);

        deleteEmployee(employee);

        return "Deleted successfully!";
    }

    private List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM workers.employees")) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setName(resultSet.getString("name"));
                employee.setPosition(resultSet.getString("position"));
                employee.setEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private void saveEmployee(Employee employee) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO workers.employees (name, position, email) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setString(3, employee.getEmail());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployeeById(long id) {
        Employee employee = new Employee();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM workers.employees WHERE id = ?")) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    employee.setId(resultSet.getLong("id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setPosition(resultSet.getString("position"));
                    employee.setEmail(resultSet.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    private void updateEmployee(Employee employee) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE workers.employees SET name = ?, position = ?, email = ? WHERE id = ?")) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setLong(4, employee.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(Employee employee) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM workers.employees WHERE id = ?")) {

            preparedStatement.setLong(1, employee.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}