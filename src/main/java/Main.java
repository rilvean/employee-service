import EmployeeService.EmployeeConsoleUI;
import EmployeeService.EmployeeService;

public static void main() {
    try {
        EmployeeService service = new EmployeeService("employees.json");
        EmployeeConsoleUI ui = new EmployeeConsoleUI(service);
        ui.start();
    } catch (Exception e) {
        System.out.println("Ошибка запуска программы: " + e.getMessage());
    }
}