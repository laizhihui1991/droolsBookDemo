package com.test.drools.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author laizhihui
 */
public class Person implements Serializable {
    private String name;
    private int age;
    private int weight;
    private int height;
    private String hobby;
    private String  className;
    private Behavior behavior;
    private Object householdRegister;
    private List payChecks;
    private Map addressMap;
    private Set classNames;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public Object getHouseholdRegister() {
        return householdRegister;
    }

    public void setHouseholdRegister(Object householdRegister) {
        this.householdRegister = householdRegister;
    }

    public List getPayChecks() {
        return payChecks;
    }

    public void setPayChecks(List payChecks) {
        this.payChecks = payChecks;
    }

    public Map getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map addressMap) {
        this.addressMap = addressMap;
    }

    public Set getClassNames() {
        return classNames;
    }

    public void setClassNames(Set classNames) {
        this.classNames = classNames;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Person() {
        System.out.println("创建Person");
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
            Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", weight=" + weight +
            ", height=" + height +
            ", className='" + className + '\'' +
            ", behavior=" + behavior +
            ", householdRegister=" + householdRegister +
            ", payChecks=" + payChecks +
            ", addressMap=" + addressMap +
            ", classNames=" + classNames +
            '}';
    }
}
