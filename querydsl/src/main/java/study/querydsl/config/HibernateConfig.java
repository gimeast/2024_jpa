package study.querydsl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * packageName     :   study.querydsl.config
 * fileName        :   HibernateConfig
 * author          :   donguk
 * date            :   2024-04-10
 * description     :   Spring Boot 3.2.1 버전 부터 application.yml에 명시적인 Dialect를 설정을 했을 시 문제가 발생한다.
 *                     해결방법: Database Platform을 설정해줌으로써 해결할 수 있다.
 * =================================================
 * DATE       |     Author      |       NOTE
 * 2024-04-10       donguk              최초 생성
 */
@Configuration
public class HibernateConfig {

    private static final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return hibernateJpaVendorAdapter;
    }

}