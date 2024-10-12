package com.kunj.config;//package com.kunj.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "userEntityManagerFactory",
//    basePackages = "com.kunj.repository",
//    transactionManagerRef = "userTransactionManager")
//public class UserDataSourceConfiguration {
//
//  @Primary
//  @Bean(name = "userDataSourceProperties")
//  @ConfigurationProperties("spring.datasource")
//  public DataSourceProperties mysqlDataSourceProperties() {
//    return new DataSourceProperties();
//  }
//  /*
//   @Primary: This tells Spring that if there are multiple beans of the same type
//   (in this case, DataSource), it should prefer this one. So, when Spring needs a DataSource,
//    it will use this bean by default.
//   */
//  @Primary
//  /** when we can create a @Bean then it will indicate a method to initiate new object
//   * and manage by the spring container
//   * configures, and initializes a new object to be managed by the Spring IoC container
//   */
//  @Bean(name = "userDataSource")
//  @ConfigurationProperties(prefix = "spring.datasource")
//  public DataSource dataSource(
//      @Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {
//    return dataSourceProperties.initializeDataSourceBuilder().build();
//  }
//  @Primary
//  @Bean(name="userEntityManagerFactory")
//  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
//      (EntityManagerFactoryBuilder  builder,
//          @Qualifier("userDatasource") DataSource dataSource){
//    return (LocalContainerEntityManagerFactoryBean) builder.withDataSource(dataSource)
//      .build();
//  }
//
//  @Primary
//  @Bean(name = "userTransactionManager")
//  @ConfigurationProperties("spring.jpa")
//  public PlatformTransactionManager platformTransactionManager(@Qualifier("userEntityManagerFactory")
//  EntityManagerFactory entityManagerFactoryBuilder){
//  return new JpaTransactionManager(entityManagerFactoryBuilder);
//  }
//}
