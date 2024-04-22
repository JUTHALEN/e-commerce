package com.alende.judith.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

public class ModelAnnotationsTest {

    static Stream<File> provideModelFiles() {
        var listFiles = new File("src/main/java/com/alende/judith/model/").listFiles();
        assertThat(listFiles).hasSizeGreaterThan(0);
        return Stream.of(listFiles).filter(f -> !f.isDirectory());
    }

    @ParameterizedTest
    @MethodSource("provideModelFiles")
    void testModelFile(File file) throws Exception {
        var parse = StaticJavaParser.parse(file);

        var fileName = file.getName();
        var className = fileName.substring(0, fileName.length() - ".java".length());
        var clazz = parse.getClassByName(className).get();

        assertThat(clazz.getAnnotationByName("Data")).as(className + " @Data").isNotEmpty();
        assertThat(clazz.getAnnotationByName("NoArgsConstructor")).as(className + " @NoArgsConstructor").isNotEmpty();
        assertThat(clazz.getAnnotationByName("AllArgsConstructor")).as(className + " @AllArgsConstructor").isNotEmpty();

        assertThat(clazz.getMethodsByName("equals")).hasSize(0);
        assertThat(clazz.getMethodsByName("hashCode")).hasSize(0);
        assertThat(clazz.getMethodsByName("toString")).hasSize(0);

    }

    void testEqualsAndHashCode(String className, ClassOrInterfaceDeclaration clazz) {

        var equalsAndHashCodeOptional = clazz.getAnnotationByName("EqualsAndHashCode");
        assertThat(equalsAndHashCodeOptional).as(className + " @EqualsAndHashCode").isNotEmpty();

        assertThat(equalsAndHashCodeOptional.get().findAll(MemberValuePair.class).stream()
                .filter(p -> "onlyExplicitlyIncluded".equals(p.getName().getIdentifier()))
                .filter(p -> p.getValue() instanceof BooleanLiteralExpr).map(p -> (BooleanLiteralExpr) p.getValue())
                .filter(BooleanLiteralExpr::getValue).findAny()).as(
                className + " @EqualsAndHashCode(onlyExplicitlyIncluded = true)").isNotEmpty();

        var id = clazz.getFieldByName("id");
        assertThat(id).as(className + ".id").isNotEmpty();

        var equalsAndHashCodeIncludeOptional = id.get().getAnnotationByName("Include");
        assertThat(equalsAndHashCodeIncludeOptional).as(className + ".id @EqualsAndHashCode.Include").isNotEmpty();

        var annotationExpr = equalsAndHashCodeIncludeOptional.get().asMarkerAnnotationExpr();
        assertThat(annotationExpr).as(className + ".id @EqualsAndHashCode.Include")
                .hasToString("@EqualsAndHashCode.Include");

        for (FieldDeclaration field : clazz.getFields()) {

            var identifier = field.getVariable(0).getName().getIdentifier();
            if ("id".equals(identifier)) {
                continue;
            }

            equalsAndHashCodeIncludeOptional = field.getAnnotationByName("Include");
            assertThat(equalsAndHashCodeIncludeOptional).as(
                    className + "." + identifier + " @EqualsAndHashCode.Include").isEmpty();
        }
    }
}

