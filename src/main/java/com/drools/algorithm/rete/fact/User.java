package com.drools.algorithm.rete.fact;

/**
 * @Author laizhihui
 * @Date 2025/1/21 11:39
 * @PackageName:com.test.drools.pojo
 * @ClassName: User
 * @Description: TODO
 * @Version 1.0
 */
public class User {

    private Integer id;

    private String name;

    private Integer age;

    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
