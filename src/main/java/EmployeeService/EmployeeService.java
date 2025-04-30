package EmployeeService;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import EmployeeService.exceptions.*;

public class EmployeeService {
    private final String _filePath;
    private final List<Employee> _employeesList;

    public List<Employee> getEmployees() {return _employeesList;}

    public EmployeeService(String filePath) throws IOException {
        _filePath = filePath;
        _employeesList = loadFromFile();
        saveToFile();
    }

    public void addEmployee(Employee employee) throws DuplicateEmployeeException, IOException {
        if (isRepeat(employee)) {
            throw new DuplicateEmployeeException("Такой объект уже существует.");
        }

        _employeesList.add(employee);
        saveToFile();
    }

    public void removeEmployee(int id) throws NotFoundEmployeeException, IOException {
        Employee employee = findEmployeeOrThrow(id);

        _employeesList.remove(employee);
        Employee.reassignIds(_employeesList);
        saveToFile();
    }

    public void updateEmployee(int id, EmployeeField field, String newValue)
            throws NotFoundEmployeeException, NullPointerException,
            IllegalArgumentException, DuplicateEmployeeException,
            IOException {
        Employee employee = findEmployeeOrThrow(id);

        if (newValue == null) {
            throw new NullPointerException("Новое значение не может быть null.");
        }

        Employee tmpEmployee = employee.clone(true);

        switch (field) {
            case FullName:
                tmpEmployee.setFullName(newValue);
                break;
            case BirthDate:
                try {
                    LocalDate localDate = LocalDate.parse(newValue);
                    tmpEmployee.setBirthDate(localDate);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Неверный формат даты. Используйте yyyy-MM-dd.");
                }
                break;
            case MaritalStatus:
                tmpEmployee.setMaritalStatus(newValue);
                break;
            case Position:
                tmpEmployee.setPosition(newValue);
                break;
            case EmploymentYear:
                try {
                    tmpEmployee.setEmploymentYear(Integer.parseInt(newValue));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Год приема на работу должен быть числом.");
                }
                break;
            default:
                throw new IllegalArgumentException("Неверное поле для обновления.");
        }

        if (isRepeat(tmpEmployee)) {
            throw new DuplicateEmployeeException("Такой объект уже существует.");
        }

        employee.setFullName(tmpEmployee.getFullName());
        employee.setBirthDate(tmpEmployee.getBirthDate());
        employee.setMaritalStatus(tmpEmployee.getMaritalStatus());
        employee.setPosition(tmpEmployee.getPosition());
        employee.setEmploymentYear(tmpEmployee.getEmploymentYear());

        saveToFile();
    }

    public List<Employee> searchEmployee(Predicate<Employee> predicate) {
        return _employeesList.stream().filter(predicate).collect(Collectors.toList());
    }

    private boolean isRepeat(Employee employee) {
        return _employeesList.contains(employee);
    }

    private List<Employee> loadFromFile() throws IOException {
        Path path = Paths.get(_filePath);

        if (Files.notExists(path)) {
            Files.createFile(path);
            Files.write(path, "[]".getBytes(StandardCharsets.UTF_8));
            return new ArrayList<>();
        }

        try (InputStream in = Files.newInputStream(path);
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            List<Employee> list = gson.fromJson(reader, new TypeToken<List<Employee>>() {}.getType());

            if (!list.isEmpty()) {
                Employee._nextId = list.getLast().getId() + 1;
            }

            return list;
        }
    }

    private void saveToFile() throws IOException {
        Path path = Paths.get(_filePath);

        try (OutputStream out = Files.newOutputStream(path);
             Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            gson.toJson(_employeesList, writer);
        }
    }

    private Employee findEmployeeOrThrow(int id) throws NotFoundEmployeeException {
        return _employeesList.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundEmployeeException("Сотрудник не найден."));
    }
}