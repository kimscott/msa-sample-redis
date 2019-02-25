package com.example.redisTemplate;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@SpringBootApplication
public class RedisTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisTemplateApplication.class, args);
	}

	/**
	 * xml 파일을 가지고 쿼리를 만들지 않는다면, 설정할 필요가 없다.
	 * 다만 xml 에서 쿼리를 만드려면 필요하다.
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		Resource[] arrResource = new PathMatchingResourcePatternResolver()
				.getResources("classpath:mapper/**/*Mapper.xml");
		// com.example.redisTemplate.Customer 를 Customer로 쓰고싶을때 설정한다.
//		sqlSessionFactoryBean.setTypeAliasesPackage("com.example.redisTemplate");

		sqlSessionFactoryBean.setMapperLocations(arrResource);
		sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return sqlSessionFactoryBean.getObject();
	}
}

