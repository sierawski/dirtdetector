package pl.rrbs.dirtdetector;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exception with message informing which fields of beans have been modified.
 */
public class DirtException extends Exception {
    public DirtException(String testName, Map<String, Collection<String>> beanToFields) {
        super(String.format("Test '%s' has modified following beans: [%s]", testName,
                beanToFields.keySet().stream()
                        .map(beanName -> String.format("{'%s' fields: %s}", beanName,
                                beanToFields.get(beanName).stream().collect(Collectors.joining(", "))))
                        .collect(Collectors.joining(", "))));
    }
}
