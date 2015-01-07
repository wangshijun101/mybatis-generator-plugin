package x.flyspace.mybatisgenerator.type;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.types.JdbcTypeNameTranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by sky91 on 12/3/14.
 */
public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {
	private static final String CUSTOM_TYPE_MAP_PROPERTY_NAME = "customTypeMap";
	protected Map<Integer, FullyQualifiedJavaType> customTypeMap;

	public CustomJavaTypeResolver() {
		customTypeMap = new HashMap<>();
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);
		String p = this.properties.getProperty(CUSTOM_TYPE_MAP_PROPERTY_NAME);
		if(p != null) {
			String[] mapStrings = p.split(",");
			if(mapStrings.length > 0) {
				for(String mapString : mapStrings) {
					String[] jdbcAndJava = mapString.split(":");
					if(jdbcAndJava.length >= 2) {
						customTypeMap.put(JdbcTypeNameTranslator.getJdbcType(jdbcAndJava[0].trim()),
										  new FullyQualifiedJavaType(jdbcAndJava[1].trim()));
					}
				}
			}
		}
	}

	@Override
	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
		FullyQualifiedJavaType answer = customTypeMap.get(introspectedColumn.getJdbcType());
		return answer != null ? answer : super.calculateJavaType(introspectedColumn);
	}
}
