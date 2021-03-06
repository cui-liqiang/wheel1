package core.bean.parsers;

import core.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.AssertUtil.Assert;

public class XmlBeanDefinitionParser {

    private static final String CONSTRUCTOR_ARG = "constructor-arg";
    private static final String VALUE = "value";
    private static final String REF = "ref";
    private static final String PROPERTY = "property";

    public List<BeanDefinition> parse(String resource) throws Exception {
        SAXReader reader = new SAXReader();

        Document doc = reader.read(this.getClass().getClassLoader().getResourceAsStream(resource));

        return getBeansFrom(doc.getRootElement().elements("bean"));
    }

    private List<BeanDefinition> getBeansFrom(List beans) throws Exception {
        List<BeanDefinition> definitions = new ArrayList<BeanDefinition>();
        for (Object o : beans) {
            Element beanDesc = (Element)o;
            String id = nullOrValue(beanDesc.attribute("id"));
            String className = nullOrValue(beanDesc.attribute("class"));
            String scope = nullOrValue(beanDesc.attribute("scope"));

            Assert(id != null && className != null, "Cannot init bean definition since it lack of \"id\" or \"class\" attribute");

            List<ParamDesc> consParams = extractConsParams(beanDesc);
            Map<String, ParamDesc> setterParams = extractSetterParams(beanDesc);

            definitions.add(new XmlBeanDefinition(id,
                                                  Class.forName(className),
                                                  scope,
                                                  consParams,
                                                  setterParams));
        }
        return definitions;
    }

    private Map<String, ParamDesc> extractSetterParams(Element beanDesc) throws Exception {
        Map<String, ParamDesc> paramParams = new HashMap<String, ParamDesc>();

        List elements = beanDesc.elements(PROPERTY);
        for (Object o : elements) {
            Element element = (Element) o;
            Attribute name = element.attribute("name");
            Attribute ref = element.attribute("ref");

            Assert(name != null && ref != null, "For setter injection, \"name\" and \"ref\" attributes are needed.");

            paramParams.put(name.getValue(), new RefParamDesc(ref.getValue()));
        }

        return paramParams;
    }

    private List<ParamDesc> extractConsParams(Element beanDesc) throws Exception {
        List<ParamDesc> consParams = new ArrayList<ParamDesc>();
        List elements = beanDesc.elements(CONSTRUCTOR_ARG);
        for (Object o : elements) {
            Element argDesc = (Element)o;
            Attribute valueAttr = argDesc.attribute(VALUE);
            Attribute refAttr = argDesc.attribute(REF);
            Assert(valueAttr != null ^ refAttr != null,
                    "There should have exactly one and only one of \"value\" or \"ref\" exists in \"constructor-arg\" tag attributes");

            consParams.add(
                valueAttr != null ? new ValueParamDesc(valueAttr.getValue()) : new RefParamDesc(refAttr.getValue())
            );
        }
        return consParams;
    }

    private String nullOrValue(Attribute attribute) {
        return attribute == null ? null : attribute.getValue();
    }
}