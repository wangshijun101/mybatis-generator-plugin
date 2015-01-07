package x.flyspace.mybatisgenerator.util;

import org.mybatis.generator.api.dom.java.*;

/**
 * Created by sky91 on 12/2/14.
 */
public class GeneratorUtils {
	public static void addPropertyToClass(TopLevelClass cl, FullyQualifiedJavaType type, String name, boolean hasSetter, boolean hasGetter) {
		Field startField = new Field(name, type);
		startField.setVisibility(JavaVisibility.PRIVATE);
		cl.addField(startField);
		if(hasSetter) {
			Method setter = new Method("set" + name.substring(0, 1).toUpperCase() + name.substring(1));
			setter.setReturnType(null);
			setter.setVisibility(JavaVisibility.PUBLIC);
			setter.addParameter(new Parameter(type, name));
			setter.addBodyLine("this." + name + "=" + name + ";");
			cl.addMethod(setter);
		}
		if(hasGetter) {
			Method getter = new Method("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
			getter.setReturnType(type);
			getter.setVisibility(JavaVisibility.PUBLIC);
			getter.addBodyLine("return " + name + ";");
			cl.addMethod(getter);
		}
	}
}
