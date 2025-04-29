package EmployeeService;

import java.time.LocalDate;

public abstract class Person {
    protected String fullName;
    protected LocalDate birthDate;

    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}