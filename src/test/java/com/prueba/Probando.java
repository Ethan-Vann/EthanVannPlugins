package com.prueba;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Probando {
    public static void main(String[] args) {
        List<Integer> kat = new ArrayList<>();
        kat.add(45);
        kat.add(76);
        kat.add(666);
        kat.add(69);
        kat.add(275);
        System.out.println(kat.stream().filter(x -> x>100).collect(Collectors.toList()));
        System.out.println(kat);
        System.out.println(kat.stream().filter(x -> x>100).map(Object::toString));
    }
}
