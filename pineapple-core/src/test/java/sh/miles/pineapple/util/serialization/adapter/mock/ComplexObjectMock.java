package sh.miles.pineapple.util.serialization.adapter.mock;

import java.util.List;
import java.util.Objects;

public class ComplexObjectMock {
    public String name;
    public int age;
    public double money;
    public List<String> properties;

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (!(object instanceof final ComplexObjectMock that)) return false;
        return age == that.age && Double.compare(money, that.money) == 0 && Objects.equals(name, that.name) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, money, properties);
    }

    public static ComplexObjectMock basic() {
        final ComplexObjectMock mock = new ComplexObjectMock();
        mock.name = "Joe";
        mock.age = 53;
        mock.money = 69420.15;
        mock.properties = List.of("East", "West");
        return mock;
    }

    @Override
    public String toString() {
        return "ComplexObjectMock{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", properties=" + properties +
                '}';
    }
}
