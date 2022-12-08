package tech.skagedal.javaaoc;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import tech.skagedal.javaaoc.aoc.App;

public class Main {
    public static void main(String[] args) {
        final var context = createApplicationContext();
        if (context.getBean(App.class) instanceof App app) {
            app.runAllDays();
        } else {
            System.err.println("Could not get App bean");
        }
    }

    private static AnnotationConfigApplicationContext createApplicationContext() {
        final var context = new AnnotationConfigApplicationContext(Config.class);
        context.scan(Main.class.getPackageName());
        context.registerShutdownHook();
        return context;
    }

    @Configuration
    @ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
    public static class Config {

    }
}
