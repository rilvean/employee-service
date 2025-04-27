package EmployeeService;

import java.time.LocalDate;
import java.util.List;

public class Employee {
    static int _nextId = 1;

    private int _id;
    private String _fullName;
    private LocalDate _birthDate;
    private String _maritalStatus;
    private String _position;
    private int _employmentYear;

    public int getId() { return _id; }
    public String getFullName() { return _fullName; }
    public LocalDate getBirthDate() { return _birthDate; }
    public String getMaritalStatus() { return _maritalStatus; }
    public String getPosition() { return _position; }
    public int getEmploymentYear() { return _employmentYear; }

    public void setFullName(String fullName) { _fullName = fullName; }
    public void setBirthDate(LocalDate birthDate) { _birthDate = birthDate; }
    public void setMaritalStatus(String maritalStatus) { _maritalStatus = maritalStatus; }
    public void setPosition(String position) { _position = position; }
    public void setEmploymentYear(int employmentYear) { _employmentYear = employmentYear; }
    void setId(int id) { _id = id; }

    public Employee(String fullName, LocalDate birthDate, String maritalStatus, String position, int employmentYear) {
        _id = _nextId++;
        _fullName = fullName;
        _birthDate = birthDate;
        _maritalStatus = maritalStatus;
        _position = position;
        _employmentYear = employmentYear;
    }

    static void reassignIds(List<Employee> employees) {
        int id = 1;
        for (Employee employee : employees) {
            employee._id = id++;
        }
        _nextId = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            Employee e = (Employee) obj;
            return _id == e._id &&
                    _employmentYear == e._employmentYear &&
                    _fullName.equals(e._fullName) &&
                    _birthDate.equals(e._birthDate) &&
                    _maritalStatus.equals(e._maritalStatus) &&
                    _position.equals(e._position);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _id +
                _employmentYear +
                _fullName.hashCode() +
                _birthDate.hashCode() +
                _maritalStatus.hashCode() +
                _position.hashCode();
    }

    public Employee clone(boolean saveId) {
        Employee clone = new Employee(_fullName, _birthDate, _maritalStatus, _position, _employmentYear);
        if (saveId) {
            clone.setId(_id);
            _nextId--;
        }
        return clone;
    }
}