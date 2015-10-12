package net.sblundy.experiments.ember;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 */
@Configuration
@ComponentScan(
        basePackages = "net.sblundy.experiments.ember.services"
)
@EnableWebMvc
@EnableJpaRepositories("net.sblundy.experiments.ember.data")
public class RootConfig {

    @Bean
    public DataSource createEmbedded() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:memory:myDB;create=true");
        return dataSource;
    }

    @Bean
    public AbstractEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(ds);

        HibernateJpaVendorAdapter jpsVenderAdapter = new HibernateJpaVendorAdapter();
        jpsVenderAdapter.setDatabasePlatform("org.hibernate.dialect.DerbyDialect");
        bean.setJpaVendorAdapter(jpsVenderAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hbm2ddl.auto", "update");
        bean.setJpaProperties(jpaProperties);
        return bean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
