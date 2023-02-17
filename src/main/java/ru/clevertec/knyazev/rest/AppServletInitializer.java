package ru.clevertec.knyazev.rest;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;

public class AppServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {AppConnectionConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}