package x.flyspace.mybatisgenerator.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import x.flyspace.mybatisgenerator.type.FileFullName;
import x.flyspace.mybatisgenerator.type.TypeFullName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky91 on 12/16/14.
 */
public class SeparateUserCodePlugin extends PluginAdapter {
	public static final String TARGET_PACKAGE_PROPERTY_NAME = "separateUserCodePlugin.targetPackage";
	public static final String SEARCH_PROPERTY_NAME = "separateUserCodePlugin.search";
	public static final String REPLACE_PROPERTY_NAME = "separateUserCodePlugin.replace";
	public static final String PREFIX_PROPERTY_NAME = "separateUserCodePlugin.prefix";
	public static final String SUFFIX_PROPERTY_NAME = "separateUserCodePlugin.suffix";
	private List<GeneratedJavaFile> generatedJavaFileList = new ArrayList<>();
	private List<GeneratedXmlFile> generatedXmlFileList = new ArrayList<>();

	@Override
	public boolean validate(List<String> strings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String userCodePackage = context.getJavaClientGeneratorConfiguration().getProperty(TARGET_PACKAGE_PROPERTY_NAME);
		if(userCodePackage == null) {
			userCodePackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
		}
		String userCodeSearch = context.getJavaClientGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String userCodeReplace = context.getJavaClientGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		String userCodePrefix = context.getJavaClientGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		String userCodeSuffix = context.getJavaClientGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		TypeFullName userInterfaceTypeFullName = new TypeFullName(userCodePackage, interfaze.getType().getShortName());
		userInterfaceTypeFullName.replaceTypeShortName(userCodeSearch, userCodeReplace).fixTypeShortName(userCodePrefix, userCodeSuffix);
		String userCodeTargetProject = context.getJavaClientGeneratorConfiguration().getTargetProject();
		File userInterfaceFile = new File(userCodeTargetProject + "/" + new FileFullName(userInterfaceTypeFullName, "java").getFileFullName());
		if(!userInterfaceFile.exists()) {
			Interface userInterface = new Interface(userInterfaceTypeFullName.getTypeFullName());
			userInterface.addImportedType(interfaze.getType());
			userInterface.addSuperInterface(interfaze.getType());
			userInterface.setVisibility(JavaVisibility.PUBLIC);
			generatedJavaFileList.add(new GeneratedJavaFile(userInterface, userCodeTargetProject, context.getJavaFormatter()));
		}
		//
		String userMapperPackage = context.getSqlMapGeneratorConfiguration().getProperty(TARGET_PACKAGE_PROPERTY_NAME);
		if(userMapperPackage == null) {
			userMapperPackage = context.getSqlMapGeneratorConfiguration().getTargetPackage();
		}
		String userMapperSearch = context.getSqlMapGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String userMapperReplace = context.getSqlMapGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		String userMapperPrefix = context.getSqlMapGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		String userMapperSuffix = context.getSqlMapGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		TypeFullName userMapperTypeFullName = new TypeFullName(userMapperPackage, userInterfaceTypeFullName.getTypeShortName());
		userMapperTypeFullName.replaceTypeShortName(userMapperSearch, userMapperReplace).fixTypeShortName(userMapperPrefix, userMapperSuffix);
		String userMapperTargetProject = context.getSqlMapGeneratorConfiguration().getTargetProject();
		File userMapperFile = new File(userMapperTargetProject + "/" + new FileFullName(userMapperTypeFullName, "xml").getFileFullName());
		if(!userMapperFile.exists()) {
			Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
			XmlElement rootElement = new XmlElement("mapper");
			rootElement.addAttribute(new Attribute("namespace", userInterfaceTypeFullName.getTypeFullName()));
			document.setRootElement(rootElement);
			generatedXmlFileList.add(new GeneratedXmlFile(document,
														  new FileFullName(userMapperTypeFullName, "xml").getFileShortName(),
														  userMapperPackage,
														  userMapperTargetProject,
														  false,
														  context.getXmlFormatter()));
		}
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		return generatedJavaFileList;
	}

	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		return generatedXmlFileList;
	}
}
