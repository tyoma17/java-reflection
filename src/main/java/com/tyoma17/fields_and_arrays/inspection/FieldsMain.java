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

package com.tyoma17.fields_and_arrays.inspection;

import java.lang.reflect.Field;

import static com.tyoma17.fields_and_arrays.inspection.FieldsMain.Category.ADVENTURE;

public class FieldsMain {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
        printDeclaredFieldsInfo(Movie.class);
        printDeclaredFieldsInfo(Movie.MovieStats.class);
        printDeclaredFieldsInfo(Category.class);

        Movie movie = new Movie("Lord of the Rings", 2001, 12.99, true, ADVENTURE);
        printDeclaredFieldsInfo(movie.getClass(), movie);

        Field minPriceStaticField = Movie.class.getDeclaredField("MINIMUM_PRICE");
        System.out.println(String.format("Static MINIMUM_PRICE value: %f", minPriceStaticField.get(null)));
    }

    public static <T> void printDeclaredFieldsInfo(Class<?> clazz) {

        for (Field field: clazz.getDeclaredFields()) {
            // includes public, protected, default (package) access, and private fields, but excludes inherited fields.
            System.out.println(String.format("Field name: %s, type: %s, is synthetic: %s",
                    field.getName(),
                    field.getType().getName(),
                    field.isSynthetic()));
        }
        System.out.println("------------------");

    }

    public static <T> void printDeclaredFieldsInfo(Class<? extends T> clazz, T instance) throws IllegalAccessException {

        for (Field field: clazz.getDeclaredFields()) {
            System.out.println(String.format("Field name: %s, type: %s, is synthetic: %s, value: %s",
                    field.getName(),
                    field.getType().getName(),
                    field.isSynthetic(),
                    field.get(instance)));
        }
        System.out.println("------------------");

    }

    public static class Movie extends Product {

        public static final double MINIMUM_PRICE = 10.99;

        private boolean isReleased;
        private Category category;
        private double actualPrice;

        public Movie(String name, int year, double price, boolean isReleased, Category category) {
            super(name, year);
            this.isReleased = isReleased;
            this.category = category;
            this.actualPrice = Math.max(price, MINIMUM_PRICE);
        }

        public class MovieStats {

            private double timesWatched;

            public MovieStats(double timesWatched) {
                this.timesWatched = timesWatched;
            }

            public double getRevenue() {
                return timesWatched * actualPrice;
            }
        }
    }

    public static class Product {

        protected String name;
        protected int year;
        protected double actualPrice;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }

    public enum Category {
        ADVENTURE,
        ACTION,
        COMEDY
    }

}
