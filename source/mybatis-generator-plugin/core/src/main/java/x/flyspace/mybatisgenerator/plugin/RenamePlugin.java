package x.flyspace.mybatisgenerator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import x.flyspace.mybatisgenerator.type.FileFullName;
import x.flyspace.mybatisgenerator.type.TypeFullName;

import java.util.List;

/**
 * Created by sky91 on 12/3/14.
 */
public class RenamePlugin extends PluginAdapter {
	public static final String SEARCH_PROPERTY_NAME = "renamePlugin.search";
	public static final String REPLACE_PROPERTY_NAME = "renamePlugin.replace";
	public static final String PREFIX_PROPERTY_NAME = "renamePlugin.prefix";
	public static final String SUFFIX_PROPERTY_NAME = "renamePlugin.suffix";

	@Override
	public boolean validate(List<String> strings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String contextSearch = context.getProperty(SEARCH_PROPERTY_NAME);
		String contextReplace = context.getProperty(REPLACE_PROPERTY_NAME);
		if(contextSearch != null && contextReplace != null) {
			replaceContext(introspectedTable, contextSearch, contextReplace);
		}
		String modelSearch = context.getJavaModelGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String modelReplace = context.getJavaModelGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		if(modelSearch != null && modelReplace != null) {
			replaceModel(introspectedTable, modelSearch, modelReplace);
		}
		String sqlMapSearch = context.getSqlMapGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String sqlMapReplace = context.getSqlMapGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		if(sqlMapSearch != null && sqlMapReplace != null) {
			replaceSqlMap(introspectedTable, sqlMapSearch, sqlMapReplace);
		}
		String clientSearch = context.getJavaClientGeneratorConfiguration().getProperty(SEARCH_PROPERTY_NAME);
		String clientReplace = context.getJavaClientGeneratorConfiguration().getProperty(REPLACE_PROPERTY_NAME);
		if(clientSearch != null && clientReplace != null) {
			replaceClient(introspectedTable, clientSearch, clientReplace);
		}
		String contextPrefix = context.getProperty(PREFIX_PROPERTY_NAME);
		String contextSuffix = context.getProperty(SUFFIX_PROPERTY_NAME);
		String modelPrefix = context.getJavaModelGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		String sqlMapPrefix = context.getSqlMapGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		String clientPrefix = context.getJavaClientGeneratorConfiguration().getProperty(PREFIX_PROPERTY_NAME);
		modelPrefix = modelPrefix != null ? modelPrefix : (contextPrefix != null ? contextPrefix : "");
		sqlMapPrefix = sqlMapPrefix != null ? sqlMapPrefix : (contextPrefix != null ? contextPrefix : "");
		clientPrefix = clientPrefix != null ? clientPrefix : (contextPrefix != null ? contextPrefix : "");
		String modelSuffix = context.getJavaModelGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		String sqlMapSuffix = context.getSqlMapGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		String clientSuffix = context.getJavaClientGeneratorConfiguration().getProperty(SUFFIX_PROPERTY_NAME);
		modelSuffix = modelSuffix != null ? modelSuffix : (contextSuffix != null ? contextSuffix : "");
		sqlMapSuffix = sqlMapSuffix != null ? sqlMapSuffix : (contextSuffix != null ? contextSuffix : "");
		clientSuffix = clientSuffix != null ? clientSuffix : (contextSuffix != null ? contextSuffix : "");
		fixModel(introspectedTable, modelPrefix, modelSuffix);
		fixSqlMap(introspectedTable, sqlMapPrefix, sqlMapSuffix);
		fixclient(introspectedTable, clientPrefix, clientSuffix);
	}

	private void replaceContext(IntrospectedTable introspectedTable, String search, String replace) {
		replaceModel(introspectedTable, search, replace);
		replaceSqlMap(introspectedTable, search, replace);
		replaceClient(introspectedTable, search, replace);
	}

	private void replaceModel(IntrospectedTable introspectedTable, String search, String replace) {
		introspectedTable.setBaseRecordType(new TypeFullName(introspectedTable.getBaseRecordType()).replaceTypeShortName(search, replace)
																								   .getTypeFullName());
		introspectedTable.setExampleType(new TypeFullName(introspectedTable.getExampleType()).replaceTypeShortName(search, replace)
																							 .getTypeFullName());
		introspectedTable.setRecordWithBLOBsType(new TypeFullName(introspectedTable.getRecordWithBLOBsType()).replaceTypeShortName(search, replace)
																											 .getTypeFullName());
		introspectedTable.setPrimaryKeyType(new TypeFullName(introspectedTable.getPrimaryKeyType()).replaceTypeShortName(search, replace)
																								   .getTypeFullName());
	}

	private void replaceSqlMap(IntrospectedTable introspectedTable, String search, String replace) {
		introspectedTable.setMyBatis3XmlMapperFileName(new FileFullName(introspectedTable.getMyBatis3XmlMapperFileName()).replaceTypeShortName(search,
																																			   replace)
																														 .getFileShortName());
		introspectedTable.setIbatis2SqlMapFileName(new FileFullName(introspectedTable.getIbatis2SqlMapFileName()).replaceTypeShortName(search,
																																	   replace)
																												 .getFileShortName());
	}

	private void replaceClient(IntrospectedTable introspectedTable, String search, String replace) {
		introspectedTable.setMyBatis3JavaMapperType(new TypeFullName(introspectedTable.getMyBatis3JavaMapperType()).replaceTypeShortName(search,
																																		 replace)
																												   .getTypeFullName());
		introspectedTable.setMyBatis3SqlProviderType(new TypeFullName(introspectedTable.getMyBatis3SqlProviderType()).replaceTypeShortName(search,
																																		   replace)
																													 .getTypeFullName());
		introspectedTable.setDAOInterfaceType(new TypeFullName(introspectedTable.getDAOInterfaceType()).replaceTypeShortName(search, replace)
																									   .getTypeFullName());
		introspectedTable.setDAOImplementationType(new TypeFullName(introspectedTable.getDAOImplementationType()).replaceTypeShortName(search,
																																	   replace)
																												 .getTypeFullName());
	}

	private void fixModel(IntrospectedTable introspectedTable, String prefix, String suffix) {
		introspectedTable.setBaseRecordType(new TypeFullName(introspectedTable.getBaseRecordType()).fixTypeShortName(prefix, suffix)
																								   .getTypeFullName());
		introspectedTable.setExampleType(new TypeFullName(introspectedTable.getExampleType()).fixTypeShortName(prefix, suffix).getTypeFullName());
		introspectedTable.setRecordWithBLOBsType(new TypeFullName(introspectedTable.getRecordWithBLOBsType()).fixTypeShortName(prefix, suffix)
																											 .getTypeFullName());
		introspectedTable.setPrimaryKeyType(new TypeFullName(introspectedTable.getPrimaryKeyType()).fixTypeShortName(prefix, suffix)
																								   .getTypeFullName());
	}

	private void fixSqlMap(IntrospectedTable introspectedTable, String prefix, String suffix) {
		introspectedTable.setMyBatis3XmlMapperFileName(new FileFullName(introspectedTable.getMyBatis3XmlMapperFileName()).fixTypeShortName(prefix,
																																		   suffix)
																														 .getFileShortName());
		introspectedTable.setIbatis2SqlMapFileName(new FileFullName(introspectedTable.getIbatis2SqlMapFileName()).fixTypeShortName(prefix, suffix)
																												 .getFileShortName());
	}

	private void fixclient(IntrospectedTable introspectedTable, String prefix, String suffix) {
		introspectedTable.setMyBatis3JavaMapperType(new TypeFullName(introspectedTable.getMyBatis3JavaMapperType()).fixTypeShortName(prefix, suffix)
																												   .getTypeFullName());
		introspectedTable.setMyBatis3SqlProviderType(new TypeFullName(introspectedTable.getMyBatis3SqlProviderType()).fixTypeShortName(prefix, suffix)
																													 .getTypeFullName());
		introspectedTable.setDAOInterfaceType(new TypeFullName(introspectedTable.getDAOInterfaceType()).fixTypeShortName(prefix, suffix)
																									   .getTypeFullName());
		introspectedTable.setDAOImplementationType(new TypeFullName(introspectedTable.getDAOImplementationType()).fixTypeShortName(prefix, suffix)
																												 .getTypeFullName());
	}
}
