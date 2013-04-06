package core.bean.parsers;

import core.BeanDefinition;
import core.IocContainer;
import core.IocContainerBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import testpackage.Demo1;
import testpackage.NoComponentAnnotation;
import testpackage.nested.Demo2;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class XmlBeanDefinitionParserTest {
    private XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();
    private IocContainer container;
    private List<BeanDefinition> definitions;

    @Before
    public void setUp() throws Exception {
        container = new IocContainerBuilder().withConfigFile("demo2-bean-definition.xml").build();
        definitions = parser.parse("demo2-bean-definition.xml");
    }

    @Test
    public void should_parse_bean_definition_with_primitive_constructor_param() throws Exception {
        BeanDefinition definition = definitions.get(0);

        assertTrue(definition.assignableTo(NoComponentAnnotation.class));
        assertTrue(definition.matchId("NoComponent"));
    }
}
