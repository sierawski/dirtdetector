package pl.rrbs.dirtdetector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Test execution listener that verifies after each test class that the beans in application context have same fields as
 * before.
 */
public class DirtDetectorTestExecutionListener extends AbstractTestExecutionListener {

    private Map<String, BeanFields> beforeBeans;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        beforeBeans = getAllBeans(testContext.getApplicationContext());
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        Map<String, BeanFields> afterBeans = getAllBeans(testContext.getApplicationContext());
        detectDirt(testContext.getTestClass().getCanonicalName(), beforeBeans, afterBeans);
    }

    private void detectDirt(String testName, Map<String, BeanFields> beforeBeans, Map<String, BeanFields> afterBeans)
            throws DirtException {
        Map<String, Collection<String>> differences = new HashMap<>();
        for (String beanName : beforeBeans.keySet()) {
            Collection<String> differentFields = BeanFields.getDifference(beforeBeans.get(beanName),
                    afterBeans.get(beanName));
            if (!differentFields.isEmpty()) {
                differences.put(beanName, differentFields);
            }
        }
        if (!differences.isEmpty()) {
            throw new DirtException(testName, differences);
        }
    }

    private Map<String, BeanFields> getAllBeans(ApplicationContext appContext) throws Exception {
        Map<String, BeanFields> beans = new HashMap<>();
        ConfigurableListableBeanFactory beanFactory = ((AbstractApplicationContext) appContext).getBeanFactory();
        for (String name : appContext.getBeanDefinitionNames()) {
            Object bean = beanFactory.getSingleton(name);
            beans.put(name, new BeanFields(bean));

        }
        return beans;
    }

}
