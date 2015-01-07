package x.flyspace.mybatisgenerator.plugin;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import x.flyspace.mybatisgenerator.type.FullyQualifiedJavaTypes;
import x.flyspace.mybatisgenerator.type.TypeFullName;
import x.flyspace.mybatisgenerator.util.GeneratorUtils;

import java.util.*;

import static x.flyspace.mybatisgenerator.plugin.RenamePlugin.*;

/**
 * Created by sky91 on 12/1/14.
 */
public class MySQLRowBoundsPlugin extends PluginAdapter {
	public static final String BASE_EXAMPLE_TYPE_SHORT_NAME = "BaseExample";
	private Map<FullyQualifiedTable, List<XmlElement>> elementsToAdd;

	public MySQLRowBoundsPlugin() {
		elementsToAdd = new HashMap<>();
	}

	@Override
	public boolean validate(List<String> strings) {
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		List<GeneratedJavaFile> javaFiles = new ArrayList<>(1);
		TopLevelClass baseExampleClass = new TopLevelClass(getBaseExampleType());
		baseExampleClass.setVisibility(JavaVisibility.PUBLIC);
		GeneratorUtils.addPropertyToClass(baseExampleClass, FullyQualifiedJavaTypes.LONG, "start", true, true);
		GeneratorUtils.addPropertyToClass(baseExampleClass, FullyQualifiedJavaTypes.LONG, "limit", true, true);
		GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(baseExampleClass,
																	context.getJavaModelGeneratorConfiguration().getTargetProject(),
																	context.getJavaFormatter());
		javaFiles.add(generatedJavaFile);
		return javaFiles;
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.setSuperClass(getBaseExampleType());
		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		generateLimitSql(element, introspectedTable);
		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		generateLimitSql(element, introspectedTable);
		return true;
	}

	protected void copyAndSaveElement(XmlElement element, FullyQualifiedTable fqt) {
		XmlElement newElement = new XmlElement(element);
		for(Iterator<Attribute> iterator = newElement.getAttributes().iterator(); iterator.hasNext(); ) {
			Attribute attribute = iterator.next();
			if("id".equals(attribute.getName())) {
				iterator.remove();
				Attribute newAttribute = new Attribute("id", attribute.getValue() + "WithRowbounds");
				newElement.addAttribute(newAttribute);
				break;
			}
		}
		List<XmlElement> elements = elementsToAdd.get(fqt);
		if(elements == null) {
			elements = new ArrayList<>();
			elementsToAdd.put(fqt, elements);
		}
		elements.add(newElement);
	}

	private void generateLimitSql(XmlElement element, IntrospectedTable introspectedTable) {
		XmlElement ifLimitE = new XmlElement("if");
		ifLimitE.addAttribute(new Attribute("test", "limit!=null"));
		XmlElement ifStartE = new XmlElement("if");
		ifStartE.addAttribute(new Attribute("test", "start!=null"));
		ifStartE.addElement(new TextElement("#{start},"));
		ifLimitE.addElement(new TextElement("limit "));
		ifLimitE.addElement(ifStartE);
		ifLimitE.addElement(new TextElement("#{limit}"));
		element.addElement(ifLimitE);
	}

	private FullyQualifiedJavaType getBaseExampleType() {
		TypeFullName baseExampleType = new TypeFullName(context.getJavaModelGeneratorConfiguration().getTargetPackage(),
														BASE_EXAMPLE_TYPE_SHORT_NAME);
		String contextSearch = context.getProperty(SEARCH_PROPERTY_NAME);
		String contextReplace = context.getProperty(REPLACE_PROPERTY_NAME);
		baseExampleType.replaceTypeShortName(contextSearch, contextReplace);
		String modelSearch = context.getJavaModelGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String modelReplace = context.getJavaModelGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		baseExampleType.replaceTypeShortName(modelSearch, modelReplace);
		String modelPrefix = context.getJavaModelGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		String modelSuffix = context.getJavaModelGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		if(modelPrefix == null) {
			modelPrefix = context.getProperty(PREFIX_PROPERTY_NAME);
		}
		if(modelSuffix == null) {
			modelSuffix = context.getProperty(SUFFIX_PROPERTY_NAME);
		}
		baseExampleType.fixTypeShortName(modelPrefix, modelSuffix);
		return new FullyQualifiedJavaType(baseExampleType.getTypeFullName());
	}
}
