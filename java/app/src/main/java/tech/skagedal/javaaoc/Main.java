package tech.skagedal.javaaoc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

    public static AnnotationConfigApplicationContext createApplicationContext() {
        final var context = new AnnotationConfigApplicationContext(Config.class);
        context.setBeanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator());
        context.scan(Main.class.getPackageName());
        context.registerShutdownHook();
        return context;
    }

    @Configuration
    public static class Config {

    }
}
