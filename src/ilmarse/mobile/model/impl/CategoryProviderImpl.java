package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import android.sax.Element;

public class CategoryProviderImpl extends AbtractCategoryProvider{
	
	List<Category> categories;
	

	public CategoryProviderImpl(String categoriesUrl) {
		super(categoriesUrl);
	}
	
	public CategoryProviderImpl(List<Category> categories) {
		super();
		if ( categories == null ) {
			throw new IllegalArgumentException("categories cannot be null");
		}
		
		this.categories = categories; 
	}
	

	@Override
	public List<Category> getCategories() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Category> retCategories = new ArrayList<Category>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = (Element) dom.getDocumentElement();
            NodeList categories = ((Document) root).getElementsByTagName(CATEGORY);
            for (int i=0;i<categories.getLength();i++){
            	//TODO get the id and subcategories
                Category category = new CategoryImpl();		
                Node item = categories.item(i);
                NodeList properties = item.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CODE)){
                        category.setCode(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(NAME)){
                        category.setName(property.getFirstChild().getNodeValue());
                    }
                    retCategories.add(category);
                }
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        return retCategories;
	}
	
	//TODO add id and subcategories
	public static final String[] fields = { "code", "name"};

	@Override
	public List<? extends Map<String, ?>> getCategoriesAsMap() {
		List<Category> categories = getCategories();
		List<Map<String, String>> transformedCats = new ArrayList<Map<String, String>>();
		for (Category t : categories) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(fields[0], t.getCode().toString());
			map.put(fields[1], t.getName().toString());
			transformedCats.add(map);
		}
		return transformedCats;
	}

	@Override
	public String[] getMapKeys() {
		return fields;
	}

}
