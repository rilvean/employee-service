package EmployeeService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import EmployeeService.exceptions.*;

public class EmployeeConsoleUI {
    private final EmployeeService _employeeService;
    private final Scanner _scanner = new Scanner(System.in);

    public EmployeeConsoleUI(EmployeeService employeeService) {
        _employeeService = employeeService;
    }

    public void start() {
        while (true) {
            printMenu();
            System.out.print("Выберите действие: ");
            String choice = _scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addEmployee();
                        break;
                    case "2":
                        viewAllEmployees();
                        break;
                    case "3":
                        updateEmployee();
                        break;
                    case "4":
                        searchEmployee();
                        break;
                    case "5":
                        removeEmployee();
                        break;
                    case "0":
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неверный ввод. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("====== Меню ======");
        System.out.println("1. Добавить сотрудника");
        System.out.println("2. Просмотреть всех сотрудников");
        System.out.println("3. Изменить сотрудника");
        System.out.println("4. Найти сотрудника");
        System.out.println("5. Удалить сотрудника");
        System.out.println("0. Выход");
    }

    private void addEmployee() throws IOException, DuplicateEmployeeException {
        System.out.print("Введите ФИО: ");
        String fullName = _scanner.nextLine();

        System.out.print("Введите дату рождения (yyyy-MM-dd): ");
        LocalDate birthDate = LocalDate.parse(_scanner.nextLine());

        System.out.print("Введите семейное положение: ");
        String maritalStatus = _scanner.nextLine();

        System.out.print("Введите должность: ");
        String position = _scanner.nextLine();

        System.out.print("Введите год приема на работу: ");
        int employmentYear = _scanner.nextInt();
        _scanner.nextLine();

        _employeeService.addEmployee(new Employee(fullName, birthDate, maritalStatus, position, employmentYear));

        System.out.println("Сотрудник добавлен успешно.");
    }

    private void viewAllEmployees() {
        List<Employee> employees = _employeeService.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("Список сотрудников пуст.");
            return;
        }
        for (Employee e : employees) {
            printEmployee(e);
        }
    }

    private void updateEmployee() throws IOException, NotFoundEmployeeException, DuplicateEmployeeException {
        System.out.print("Введите ID сотрудника для изменения: ");
        int id = _scanner.nextInt();
        _scanner.nextLine();

        printSelectField();
        String fieldChoice = _scanner.nextLine();

        EmployeeField field;
        switch (fieldChoice) {
            case "1":
                field = EmployeeField.FullName;
                break;
            case "2":
                field = EmployeeField.BirthDate;
                break;
            case "3":
                field = EmployeeField.MaritalStatus;
                break;
            case "4":
                field = EmployeeField.Position;
                break;
            case "5":
                field = EmployeeField.EmploymentYear;
                break;
            default:
                System.out.println("Неверный выбор поля.");
                return;
        }

        System.out.print("Введите новое значение: ");
        String newValue = _scanner.nextLine();

        _employeeService.updateEmployee(id, field, newValue);

        System.out.println("Сотрудник успешно обновлен.");
    }

    private void searchEmployee() {
        printSelectField();
        String fieldChoice = _scanner.nextLine();

        System.out.print("Введите значение для поиска: ");
        String searchValue = _scanner.nextLine();

        List<Employee> results;

        switch (fieldChoice) {
            case "1":
                results = _employeeService.searchEmployee(e -> e.getFullName().equalsIgnoreCase(searchValue));
                break;
            case "2":
                try {
                    LocalDate date = LocalDate.parse(searchValue);
                    results = _employeeService.searchEmployee(e -> e.getBirthDate().equals(date));
                } catch (Exception e) {
                    System.out.println("Неверный формат даты.");
                    return;
                }
                break;
            case "3":
                results = _employeeService.searchEmployee(e -> e.getMaritalStatus().equalsIgnoreCase(searchValue));
                break;
            case "4":
                results = _employeeService.searchEmployee(e -> e.getPosition().equalsIgnoreCase(searchValue));
                break;
            case "5":
                try {
                    int year = Integer.parseInt(searchValue);
                    results = _employeeService.searchEmployee(e -> e.getEmploymentYear() == year);
                } catch (Exception e) {
                    System.out.println("Год должен быть числом.");
                    return;
                }
                break;
            default:
                System.out.println("Неверный выбор поля.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("Сотрудники не найдены.");
        } else {
            for (Employee e : results) {
                printEmployee(e);
            }
        }
    }

    private void removeEmployee() throws IOException, NotFoundEmployeeException {
        System.out.print("Введите ID сотрудника для удаления: ");
        int id = Integer.parseInt(_scanner.nextLine());

        _employeeService.removeEmployee(id);

        System.out.println("Сотрудник успешно удален.");
    }

    private void printEmployee(Employee e) {
        System.out.println("------------------------");
        System.out.println("ID: " + e.getId());
        System.out.println("ФИО: " + e.getFullName());
        System.out.println("Дата рождения: " + e.getBirthDate());
        System.out.println("Семейное положение: " + e.getMaritalStatus());
        System.out.println("Должность: " + e.getPosition());
        System.out.println("Год приема на работу: " + e.getEmploymentYear());
        System.out.println("------------------------");
    }

    private void printSelectField() {
        System.out.println("Выберите поле: ");
        System.out.println("1. ФИО");
        System.out.println("2. Дата рождения");
        System.out.println("3. Семейное положение");
        System.out.println("4. Должность");
        System.out.println("5. Год приема на работу");
        System.out.print("Ваш выбор: ");
    }
}