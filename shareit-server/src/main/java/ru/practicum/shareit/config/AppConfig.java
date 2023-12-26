package ru.practicum.shareit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("ru.practicum.shareit")
@EnableTransactionManagement
public class AppConfig {
}
