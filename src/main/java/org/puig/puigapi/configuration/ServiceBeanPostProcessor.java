package org.puig.puigapi.configuration;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ServiceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (bean instanceof PersistenceService<?, ?, ?> service)
            if (service.getType() == null)
                throw new RuntimeException("PersistenceService %s must use PuigService annotation or define property type"
                        .formatted(beanName));

        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean,
                                                  @NotNull String beanName)
            throws BeansException {
        processPuigService(bean);

        return bean;
    }

    public void processPuigService(@NotNull Object bean) {
        if (!bean.getClass().isAnnotationPresent(PuigService.class)) return;

        PuigService puigServiceAnnotation = bean.getClass().getAnnotation(PuigService.class);
        Class<?> type = puigServiceAnnotation.value();
        ((PersistenceService<?, ?, ?>) bean).setType(type);
    }
}
