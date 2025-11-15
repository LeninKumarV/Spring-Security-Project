//package com.example.security.securityProject.datasource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.DataSourceInitializer;
//import org.springframework.jdbc.datasource.init.DatabasePopulator;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class datasouceConnection {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public DataSourceInitializer initializer(){
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(new ClassPathResource("schema.sql"));
//        initializer.setDatabasePopulator(populator);
//        return initializer;
//    }
//}
