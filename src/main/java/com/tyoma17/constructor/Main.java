/*
 *  MIT License
 *
 *  Copyright (c) 2020 Michael Pogrebinsky - Java Reflection - Master Class
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tyoma17.constructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        printConstructorsData(Person.class);
        printConstructorsData(Address.class);

        Person person = createInstanceWithArguments(Person.class);
        System.out.println(person);

        Person person2 = createInstanceWithArguments(Person.class, "John", 20);
        System.out.println(person2);

        Address address = createInstanceWithArguments(Address.class, "First Street", 10);
        Person person3 = createInstanceWithArguments(Person.class, address, "John", 20);
        System.out.println(person3);
    }

    public static <T> T createInstanceWithArguments(Class<T> clazz, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {

            if (constructor.getParameterTypes().length == args.length) {
                return (T) constructor.newInstance(args);
            }
        }

        System.out.println("An appropriate constructor was not found");
        return null;
    }

    public static void printConstructorsData(Class<?> clazz) {

        // get all declared constructors (public and non-public)
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        System.out.println(String.format("Class %s has %d declared constructors",
                clazz.getSimpleName(), constructors.length));

        for (int i = 0; i < constructors.length; i++) {

            Class<?>[] parameterTypes = constructors[i].getParameterTypes();
            List<String> parameterTypeNames = Arrays.stream(parameterTypes)
                    .map(Class::getSimpleName)
                    .collect(toList());

            System.out.println(parameterTypeNames);
        }

        System.out.println("------------------");
    }

    public static class Person {

        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            name = "anonymous";
            age = 0;
            address = null;
        }

        public Person(String name) {
            this.name = name;
            age = 0;
            address = null;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            address = null;
        }

        public Person(Address address, String name, int age) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "address=" + address +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Address {

        private String street;
        private int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", number=" + number +
                    '}';
        }
    }
}
