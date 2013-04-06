package core.bean.parsers;

import core.BeanDefinition;
import core.IocContainer;
import core.IocContainerBuilder;
import org.junit.Before;
import org.junit.Test;
import testpackage.NoComponentAnnotation;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class XmlBeanDefinitionParserTest {
    private XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();
    private List<BeanDefinition> definitions;

    @Before
    public void setUp() throws Exception {
        definitions = parser.parse("bean-definition.xml");
    }

    @Test
    public void should_parse_bean_definition_with_primitive_constructor_param() throws Exception {
        BeanDefinition definition = definitions.get(0);

        assertTrue(definition.assignableTo(NoComponentAnnotation.class));
        assertTrue(definition.matchId("NoComponent"));
    }
}
