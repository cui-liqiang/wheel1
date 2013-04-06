package core.bean.parsers;

import core.BeanDefinition;
import core.IocContainer;
import core.IocContainerBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import testpackage.NoComponentAnnotation;
import testpackage.nested.Demo2;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class XmlBeanDefinitionParserTest {
    private XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();
    private IocContainer container;

    @Before
    public void setUp() throws Exception {
        container = new IocContainerBuilder().withConfigFile("demo2-bean-definition.xml").build();
    }

    @Test
    public void should_parser_bean_definition_with_primitive_constructor_param_from_xml() throws Exception {
        List<BeanDefinition> definitions = parser.parse("demo2-bean-definition.xml");
        Object o = definitions.get(0).getBean(container);
        assertTrue(o instanceof NoComponentAnnotation);

        NoComponentAnnotation bean = (NoComponentAnnotation) o;

        assertEquals(5, bean.getValue());
    }
}
